#!/bin/sh

# Inspired by https://github.com/keycloak/keycloak-containers/blob/d4ce446dde3026f89f66fa86b58c2d0d6132ce4d/server/tools/x509.sh

import_ca_bundle() {
  local X509_CRT_DELIMITER="/-----BEGIN CERTIFICATE-----/"
  local PASSWORD=changeit
  local TEMPORARY_CERTIFICATE="temporary_ca.crt"
  if [ -n "${X509_CA_BUNDLE}" ]; then
    pushd /tmp >& /dev/null
    echo "Importing CA bundle..."
    # We use cat here, so that users could specify multiple CA Bundles using space or even wildcard:
    # X509_CA_BUNDLE=/var/run/secrets/kubernetes.io/serviceaccount/*.crt
    # Note, that there is no quotes here, that's intentional. Once can use spaces in the $X509_CA_BUNDLE like this:
    # X509_CA_BUNDLE=/ca.crt /ca2.crt
    cat ${X509_CA_BUNDLE} > ${TEMPORARY_CERTIFICATE}
    csplit -s -z -f crt- "${TEMPORARY_CERTIFICATE}" "${X509_CRT_DELIMITER}" '{*}'
    for CERT_FILE in crt-*; do
      keytool -import -noprompt -cacerts -file "${CERT_FILE}" \
        -storepass "${PASSWORD}" -alias "service-${CERT_FILE}" >& /dev/null
    done
    popd >& /dev/null
  fi
}

import_ca_bundle

if [ -n "$1" ]; then
  exec "$@"
fi

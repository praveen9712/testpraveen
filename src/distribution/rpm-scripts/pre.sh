#!/bin/sh

systemctl stop tomcat >/dev/null 2>&1 || true
rm -rf /var/cache/tomcat/temp/* >/dev/null 2>&1 || true
rm -rf /var/cache/tomcat/work/* >/dev/null 2>&1 || true
rm -rf /usr/share/tomcat/webapps/ROOT >/dev/null 2>&1 || true

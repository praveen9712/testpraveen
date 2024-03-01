#!/bin/sh

# Final rpm being removed, clean up. No %pre to do cleanup
if [ $1 -eq 0 ]
then
        systemctl stop tomcat >/dev/null 2>&1 || true
        rm -rf /var/cache/tomcat/temp/* >/dev/null 2>&1 || true
        rm -rf /var/cache/tomcat/work/* >/dev/null 2>&1 || true
        rm -rf /usr/share/tomcat/webapps/ROOT >/dev/null 2>&1 || true
fi

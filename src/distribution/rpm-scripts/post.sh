#!/bin/sh

systemctl enable tomcat >/dev/null 2>&1 || true
systemctl start tomcat >/dev/null 2>&1 || true

#!/bin/sh
UIMA_CLASSPATH=$UIMA_CLASSPATH:$UIMA_HOME/docs/examples/resources:$UIMA_HOME/lib/uima_core.jar:$UIMA_HOME/lib/uima_cpe.jar:$UIMA_HOME/lib/uima_jcas_builtin_types.jar:$UIMA_HOME/lib/uima_tools.jar:$UIMA_HOME/lib/uima_examples.jar:$UIMA_HOME/lib/uima_adapter_messaging.jar:$UIMA_HOME/lib/uima_adapter_soap.jar:$UIMA_HOME/lib/uima_adapter_vinci.jar:$UIMA_HOME/lib/uima_search.jar:$UIMA_HOME/lib/juru.jar:$UIMA_HOME/lib/siapi.jar:$CATALINA_HOME/webapps/axis/WEB-INF/lib/axis.jar:$CATALINA_HOME/webapps/axis/WEB-INF/lib/commons-discovery.jar:$CATALINA_HOME/webapps/axis/WEB-INF/lib/commons-discovery-0.2.jar:$CATALINA_HOME/webapps/axis/WEB-INF/lib/commons-logging.jar:$CATALINA_HOME/webapps/axis/WEB-INF/lib/commons-logging-1.0.4.jar:$CATALINA_HOME/webapps/axis/WEB-INF/lib/jaxrpc.jar:$CATALINA_HOME/webapps/axis/WEB-INF/lib/saaj.jar:$CATALINA_HOME/webapps/axis/WEB-INF/lib/activation.jar:$UIMA_HOME/lib/vinci/jVinci.jar:$UIMA_HOME/lib/xml.jar:$UIMA_HOME/lib/dlt.jar:$UIMA_HOME/lib/an_dlt.jar:$UIMA_HOME/lib/icu4j.jar:$CLASSPATH
#also set default values for VNS_HOST and VNS_PORT
if [ "$VNS_HOST" = "" ];
then
  VNS_HOST=localhost
fi
if [ "$VNS_PORT" = "" ];
then
  VNS_PORT=9000
fi
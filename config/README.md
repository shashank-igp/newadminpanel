Configuration for the application.

Examples:

Server properties (/opt/start.ini)

SOLR_URI=http://localhost:8983/solr/
DB_DRIVER=com.mysql.jdbc.Driver
RO_DB_URI=localhost
RO_DB_USER=root
RO_DB_PASS=123
RO_DB_HOST=localhost
RO_DB_PORT=3306
RO_DB_NAME=igpdev
RW_DB_URI=localhost
RW_DB_USER=root
RW_DB_PASS=123
RW_DB_HOST=localhost
RW_DB_PORT=3306
RW_DB_NAME=igpdev
REDIS_HOST=localhost
REDIS_PORT=6379
SOLR_CORE=igp



Redis properties (/opt/redis.properties)

PRODUCT_KEY=api:product:
USER_KEY=api:user:


Messages properties (/opt/messages.properties)

ENTITY_FOUND=Resource found.
ENTITY_NOT_FOUND=Resource not found.

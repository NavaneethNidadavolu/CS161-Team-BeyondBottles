import mysql.connector
from mysql.connector import Error
from mysql.connector import pooling
from mysql.connector.pooling import MySQLConnectionPool


con_pool = MySQLConnectionPool(pool_name='my_connection_pool', pool_size=10,
                               pool_reset_session=True, host='150.230.40.168',port='5000', database='beyondbottles', user='root', password='password')

# con_pool = MySQLConnectionPool(pool_name='my_connection_pool', pool_size=10,
#                                pool_reset_session=True, host='localhost', database='upscpre', user='root', password='')

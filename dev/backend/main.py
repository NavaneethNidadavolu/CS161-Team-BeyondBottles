import json

import flask
from flask import Flask, request, jsonify
import os
from flask_cors import CORS

# from connection import con_pool
from dotenv import load_dotenv
import psycopg2
from flask import jsonify
from datetime import datetime



app = flask.Flask(__name__)
app.config["DEBUG"] = True
CORS(app)

load_dotenv()

# Defining database connection parameters
POSTGRES_USER = os.getenv("POSTGRES_USER")
POSTGRES_HOST = os.getenv("POSTGRES_HOST")
POSTGRES_PASSWORD = os.getenv("POSTGRES_PASSWORD")
POSTGRES_DATABASE = os.getenv("POSTGRES_DATABASE")

db_params = {
    "host": POSTGRES_HOST,
    "database": POSTGRES_DATABASE,
    "user": POSTGRES_USER,
    "password": POSTGRES_PASSWORD,
    "port": "5432"
}

# Define a function to connect to the database
def connect_to_db():
    try:
        connection = psycopg2.connect(**db_params)
        cursor = connection.cursor()
        print("Connected to the database.")
        return connection, cursor
    except (Exception, psycopg2.Error) as error:
        print(f"Error connecting to the database: {error}")
        return None, None

# Define a function to close the database connection
def close_db_connection(connection, cursor):
    if connection:
        cursor.close()
        connection.close()
        print("Database connection closed.")



@app.route('/', methods=['GET', 'POST'])
def home():
    return "Hello"

# @app.route('/questions', methods=['GET'])
# def getquestions():
#     if request.method == "GET":

#         db2 = con_pool.get_connection()
#         cur = db2.cursor()
#         sql_query = "select q.id, q.question,u.username,q.upvotes,TIMESTAMPDIFF(hour,curtime(),q.created_at) as time," \
#                     "count(c.comment) as comments from questions q inner join users u on " \
#                     "q.user_id = u.id left join comments c on q.id = c.question_id " \
#                     "group by q.id,q.question,u.username,q.upvotes,q.created_at order by abs(time)"

#         cur.execute(sql_query)
#         data = cur.fetchall()
#         cur.close()
#         db2.close()

#         json_data = []

#         for d in data:
#             if (abs(d[4]) >= 24):
#                 days = str(int(abs(d[4] / 24)))
#                 if (days == '1'):
#                     time = days + ' day ago'
#                 else:
#                     time = days + ' days ago'
#             else:
#                 hours = str(abs(d[4]))
#                 time = hours + ' hours ago'
#             json_data.append(
#                 {"id": d[0], 'question': d[1], 'username': d[2], 'upvotes': d[3], 'comments': d[5], 'time': time})

#         return jsonify(json_data)


# Define the /questions route
@app.route('/questions', methods=['GET'])
def get_questions():
    if request.method == "GET":
        try:
            # Get user ID from request or session, assuming user is authenticated
            user_id = request.args.get('userid', type=int)  # Modify according to your authentication mechanism

            # Get a connection from the connection pool
            connection, cursor = connect_to_db()

            # Define the SQL query
            sql_query = """
                SELECT 
                    q.id, 
                    q.question, 
                    u.username, 
                    COUNT(DISTINCT l.user_id) AS upvotes, 
                    ROUND(EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP - q.created_at)) / 36000, 1) AS time, 
                    COUNT(DISTINCT c.comment) AS comments,
                    EXISTS (SELECT 1 FROM question_likes WHERE question_id = q.id AND user_id = %s) AS isLiked
                FROM 
                    questions q 
                INNER JOIN 
                    users u ON q.user_id = u.id 
                LEFT JOIN 
                    comments c ON q.id = c.question_id 
                LEFT JOIN 
                    question_likes l ON q.id = l.question_id 
                GROUP BY 
                    q.id, q.question, u.username, q.created_at 
                ORDER BY 
                    time
            """
            # Execute the SQL query
            cursor.execute(sql_query, (user_id,))

            # Fetch all rows from the result set
            data = cursor.fetchall()

            # Close the cursor and connection
            cursor.close()
            connection.close()

            # Process the data
            json_data = []
            for d in data:
                if abs(d[4]) >= 24:
                    days = str(int(abs(d[4] / 24)))
                    time = days + (' day ago' if days == '1' else ' days ago')
                else:
                    hours = str(abs(d[4]))
                    time = hours + ' hours ago'
                json_data.append({
                    "id": d[0],
                    "question": d[1],
                    "username": d[2],
                    "upvotes": d[3],
                    "comments": d[5],
                    "time": time,
                    "isLiked": d[6]  # Add isLiked column to the JSON
                })

            # Return the JSON response
            return jsonify(json_data)

        except (Exception, psycopg2.Error) as error:
            print(f"Error fetching questions: {error}")
            return {'message': 'Error fetching questions'}, 500


@app.route('/getcomments', methods=['GET'])
def getcomments():
    try:
        if request.method == "GET":
            questionid = request.args.get('questionid', type=int)

            # Get a connection from the connection pool
            connection, cursor = connect_to_db()

            # Execute the SQL query to retrieve comments
            sql_query = """
                SELECT c.id, c.comment, u.username, EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP - c.created_at)) / 3600 AS time
                FROM comments c
                INNER JOIN users u ON c.user_id = u.id
                WHERE c.question_id = %s
                ORDER BY ABS(EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP - c.created_at)) / 3600)
            """
            cursor.execute(sql_query, (questionid,))
            data = cursor.fetchall()

            # Process the data
            json_data = []
            for d in data:
                # Convert the absolute value of time to integer
                time_hours = int(abs(d[3]))

                if time_hours >= 24:
                    days = str(time_hours // 24)  # Calculate the number of days
                    time = days + (' day ago' if days == '1' else ' days ago')
                else:
                    time = str(time_hours) + ' hours ago'

                json_data.append({'id': d[0], 'comment': d[1], 'username': d[2], 'time': time})

            # Close the cursor and connection
            cursor.close()
            connection.close()

            return jsonify(json_data)

    except (Exception, psycopg2.Error) as error:
        print("Error:", error)
        return jsonify({'error': 'Internal server error'}), 500


# @app.route('/getcomments', methods=['GET'])
# def getcomments():
#     if request.method == "GET":

#         questionid = request.args.get('questionid', type=int)

#         db2 = con_pool.get_connection()
#         cur = db2.cursor()
#         sql_query = "select c.comment,u.username,TIMESTAMPDIFF(hour,curtime(),c.created_at) as time from comments c " \
#                     "inner join users u on c.user_id = u.id where c.question_id = %d order by abs(time);" % (
#             questionid)

#         cur.execute(sql_query)
#         data = cur.fetchall()
#         cur.close()
#         db2.close()

#         json_data = []
#         for d in data:
#             if (abs(d[2]) >= 24):
#                 days = str(int(abs(d[2] / 24)))
#                 if (days == '1'):
#                     time = days + ' day ago'
#                 else:
#                     time = days + ' days ago'
#             else:
#                 hours = str(abs(d[2]))
#                 time = hours + ' hours ago'
#             json_data.append({'comment': d[0], 'username': d[1], 'time': time})

#         print(json_data)
#         return jsonify(json_data)

@app.route('/addquestion', methods=['POST'])
def addquestion():
    try:
        if request.method == "POST":
            json_data = request.get_json()

            question = json_data['question']
            user_id = json_data['userid']

            # Get a connection from the connection pool
            connection, cursor = connect_to_db()

            # Execute the SQL query to insert the question
            sql_query = "INSERT INTO questions (question, user_id, upvotes, created_at) VALUES (%s, %s, 0, NOW())"
            cursor.execute(sql_query, (question, user_id))

            # Commit the transaction
            connection.commit()

            # Close cursor and connection
            cursor.close()
            connection.close()

            # Prepare response data
            json_data = [{'status': "success"}]
            return jsonify(json_data)

    except (Exception, psycopg2.Error) as error:
        print("Error:", error)
        return jsonify({'error': 'Internal server error'}), 500

# @app.route('/addquestion', methods=['POST'])
# def addquestion():
#     if request.method == "POST":
        
#         json_data = request.get_json()

#         question = json_data['question']
#         user_id = json_data['userid']
        
#         print(question)
#         print(user_id)

#         db2 = con_pool.get_connection()
#         cur = db2.cursor()
#         sql_query = "insert into questions(question,user_id,upvotes,created_at) " \
#                     "values('%s',%d,0,curtime())" % (question, user_id)
#         cur.execute(sql_query)
#         db2.commit()
#         cur.close()
#         db2.close()

#         json_data = [{'status': "success"}]
#         print(json_data)

#         return jsonify(json_data)

# @app.route('/register', methods=['POST'])
# def register():
#     db2 = con_pool.get_connection()
#     cur = db2.cursor()

#     try:
#         if request.method == "POST":
#             print(request.args.get('username', type=str))
#             username = request.args.get('username', type=str)
#             emailid = request.args.get('email', type=str)
#             password = request.args.get('password', type=str)

#             sql_query = "insert into users(username,emailid,password) values('%s','%s','%s');" % (
#                 username, emailid, password)

#             cur.execute(sql_query)
#             db2.commit()

#             query = "SELECT id, username, emailid FROM users WHERE emailid = '%s'" % emailid
#             cur.execute(query)
#             data = cur.fetchone()
#             json_data = []
            
#             print(data)

#             content = {'id': data[0], 'username': data[1], 'email': data[2], 'error': 'none'}
#             json_data.append(content)

#             cur.close()
#             db2.close()

#             print(json_data)

#             return jsonify(json_data)
    
#     except Exception as e:
#         print("Error:", e)
#         cur.close()
#         db2.close()
#         return jsonify({'error': 'Internal server error'}), 500

@app.route('/register', methods=['POST'])
def register():
    try:
        if request.method == "POST":
            # Extract data from request body
            username = request.args.get('username', type=str)
            emailid = request.args.get('email', type=str)
            password = request.args.get('password', type=str)

            # Get a connection from the connection pool
            connection, cursor = connect_to_db()
            
            if connection is None or cursor is None:
                raise Exception("Failed to connect to the database")

            # Execute the SQL query to insert user data
            sql_query = "INSERT INTO users (username, emailid, password) VALUES (%s, %s, %s) RETURNING id, username, emailid"
            cursor.execute(sql_query, (username, emailid, password))

            # Fetch the inserted user data
            data = cursor.fetchone()

            # Commit the transaction
            connection.commit()

            # Close cursor and connection
            cursor.close()
            connection.close()

            # Prepare response data
            json_data = [{
                'id': data[0],
                'username': data[1],
                'email': data[2],
                'error': 'none'
            }]

            return jsonify(json_data)

    except (Exception, psycopg2.Error) as error:
        print("Error:", error)
        return jsonify({'error': 'Internal server error'}), 500

@app.route('/login', methods=['POST'])
def login():
    try:
        if request.method == "POST":
            # Extract data from request body
            username = request.args.get('username', type=str)
            password = request.args.get('password', type=str)

            # Get a connection from the connection pool
            connection, cursor = connect_to_db()

            if connection is None or cursor is None:
                raise Exception("Failed to connect to the database")

            # Execute the SQL query to fetch user data
            sql_query = "SELECT id, username, emailid FROM users WHERE username = %s AND password = %s"
            cursor.execute(sql_query, (username, password))

            # Fetch the user data
            data = cursor.fetchone()

            if data is None:
                # Close cursor and connection
                cursor.close()
                connection.close()
                return jsonify({'error': 'Invalid username or password'}), 401

            # Prepare response data
            json_data = [{
                'id': data[0],
                'username': data[1],
                'email': data[2],
                'error': 'none'
            }]

            # Close cursor and connection
            cursor.close()
            connection.close()

            return jsonify(json_data)

    except (Exception, psycopg2.Error) as error:
        print("Error:", error)
        return jsonify({'error': 'Internal server error'}), 500

@app.route('/addcomment', methods=['POST'])
def addcomment():
    try:
        if request.method == "POST":
            json_data = request.get_json()

            question_id = json_data['questionid']
            comment = json_data['comment']
            user_id = json_data['userid']

            # Get a connection from the connection pool
            connection, cursor = connect_to_db()

            # Execute the SQL query to insert the comment
            sql_query = "INSERT INTO comments (question_id, comment, user_id, created_at) VALUES (%s, %s, %s, NOW())"
            cursor.execute(sql_query, (question_id, comment, user_id))

            # Commit the transaction
            connection.commit()

            # Close cursor and connection
            cursor.close()
            connection.close()

            # Prepare response data
            json_data = [{'status': "success"}]
            return jsonify(json_data)

    except (Exception, psycopg2.Error) as error:
        print("Error:", error)
        return jsonify({'error': 'Internal server error'}), 500

@app.route('/upvote', methods=['POST'])
def upvote():
    try:
        if request.method == "POST":
            question_id = request.args.get('questionid', type=int)
            user_id = request.args.get('userid', type=int)  # Assuming you have user authentication and get the user ID

            # Get a connection from the connection pool
            connection, cursor = connect_to_db()

            # Execute the SQL query to insert a new row into question_likes
            sql_query_likes = "INSERT INTO question_likes (question_id, user_id, created_at) VALUES (%s, %s, %s)"
            cursor.execute(sql_query_likes, (question_id, user_id, datetime.now()))

            # Commit the transaction
            connection.commit()

            # Close cursor and connection
            cursor.close()
            connection.close()

            # Prepare response data
            json_data = [{'status': "success"}]
            return jsonify(json_data)

    except (Exception, psycopg2.Error) as error:
        print("Error:", error)
        return jsonify({'error': 'Internal server error'}), 500
 
@app.route('/downvote', methods=['POST'])
def downvote():
    try:
        if request.method == "POST":
            question_id = request.args.get('questionid', type=int)
            user_id = request.args.get('userid', type=int)

            # Get a connection from the connection pool
            connection, cursor = connect_to_db()

            # Execute the SQL query to delete the like entry from question_likes table
            sql_query = "DELETE FROM question_likes WHERE question_id = %s AND user_id = %s"
            cursor.execute(sql_query, (question_id, user_id))

            # Commit the transaction
            connection.commit()

            # Close cursor and connection
            cursor.close()
            connection.close()

            # Prepare response data
            json_data = [{'status': "success"}]
            return jsonify(json_data)

    except (Exception, psycopg2.Error) as error:
        print("Error:", error)
        return jsonify({'error': 'Internal server error'}), 500

# @app.route('/leaderboard', methods=['GET'])
# def leaderboard():
#     try:
#         # Get a connection from the connection pool
#         connection, cursor = connect_to_db()

#         if connection is None or cursor is None:
#             raise Exception("Failed to connect to the database")

#         # Execute SQL query to get leaderboard data
#         sql_query = """
#             SELECT u.username, 
#                    COUNT(DISTINCT c.id) * 3 + COUNT(DISTINCT ql.id) AS score
#             FROM users u
#             LEFT JOIN comments c ON u.id = c.user_id
#             LEFT JOIN question_likes ql ON u.id = ql.user_id
#             GROUP BY u.id
#             ORDER BY score DESC
#         """
#         cursor.execute(sql_query)
#         data = cursor.fetchall()

#         # Process the data
#         leaderboard_data = []
#         for d in data:
#             leaderboard_data.append({
#                 'username': d[0],
#                 'score': d[1]
#             })

#         # Close the cursor and connection
#         cursor.close()
#         connection.close()

#         return jsonify(leaderboard_data)

#     except (Exception, psycopg2.Error) as error:
#         print("Error:", error)
#         return jsonify({'error': 'Internal server error'}), 500

@app.route('/leaderboard', methods=['GET'])
def leaderboard():
    try:
        # Get a connection from the connection pool
        connection, cursor = connect_to_db()

        if connection is None or cursor is None:
            raise Exception("Failed to connect to the database")

        # Execute SQL query to get leaderboard data for top 10 entries
        sql_query = """
            SELECT u.id, 
                   u.username, 
                   RANK() OVER (ORDER BY (COUNT(DISTINCT c.id) * 3 + COUNT(DISTINCT ql.id)) DESC) AS rank,
                   (COUNT(DISTINCT c.id) * 3 + COUNT(DISTINCT ql.id)) AS score
            FROM users u
            LEFT JOIN comments c ON u.id = c.user_id
            LEFT JOIN question_likes ql ON u.id = ql.user_id
            GROUP BY u.id
            ORDER BY score DESC
            LIMIT 10
        """
        cursor.execute(sql_query)
        data = cursor.fetchall()

        # Process the data
        leaderboard_data = []
        for rank, d in enumerate(data, start=1):
            leaderboard_data.append({
                'id': d[0],
                'username': d[1],
                'rank': str(rank),  # Convert rank to string
                'score': str(d[3])  # Convert score to string
            })

        # Close the cursor and connection
        cursor.close()
        connection.close()

        return jsonify(leaderboard_data)

    except (Exception, psycopg2.Error) as error:
        print("Error:", error)
        return jsonify({'error': 'Internal server error'}), 500

@app.route('/addblog', methods=['POST'])
def add_blog():
    try:
        # Get the blog data from the request
        content_header = request.json.get('content_header')
        content = request.json.get('content')
        link = request.json.get('link')
        type = request.json.get('type')

        # Get a connection from the connection pool
        connection, cursor = connect_to_db()

        if connection is None or cursor is None:
            raise Exception("Failed to connect to the database")

        # Execute SQL query to insert the blog data
        sql_query = """
            INSERT INTO blogs (content_header, content, link, type)
            VALUES (%s, %s, %s, %s)
            RETURNING id, content_header, content, link, type
        """
        cursor.execute(sql_query, (content_header, content, link, type))
        new_blog_id = cursor.fetchone()
        

        # Commit the transaction
        connection.commit()

        # Close the cursor and connection
        cursor.close()
        connection.close()
        
        blog_details = {
            'id': new_blog_id[0],
            'contentHeader': new_blog_id[1],
            'content': new_blog_id[2],
            'link': new_blog_id[3],
            'type': new_blog_id[4]
        }

        return jsonify(blog_details)

    except (Exception, psycopg2.Error) as error:
        print("Error:", error)
        return jsonify({'error': 'Failed to add blog entry'}), 500
 
@app.route('/getblogs', methods=['GET'])
def get_blogs_by_type():
    try:
        # Get the type filter from the request
        blog_type = request.args.get('type')

        # Get a connection from the connection pool
        connection, cursor = connect_to_db()

        if connection is None or cursor is None:
            raise Exception("Failed to connect to the database")

        # Execute SQL query to fetch blogs by type
        sql_query = """
            SELECT id, content_header, content, link, type
            FROM blogs
            WHERE type = %s
        """
        cursor.execute(sql_query, (blog_type,))
        blog_entries = cursor.fetchall()

        # Close the cursor and connection
        cursor.close()
        connection.close()

        # Prepare the response data
        blogs_data = []
        for blog in blog_entries:
            blog_details = {
                'id': blog[0],
                'content_header': blog[1],
                'content': blog[2],
                'link': blog[3],
                'type': blog[4]
            }
            blogs_data.append(blog_details)

        return jsonify(blogs_data)

    except (Exception, psycopg2.Error) as error:
        print("Error:", error)
        return jsonify({'error': 'Failed to retrieve blogs by type'}), 500


@app.route('/addadmin', methods=['POST'])
def add_admin():
    try:
        # Get admin data from the request
        username = request.json.get('username')
        email = request.json.get('email')
        password = request.json.get('password')

        # Get a connection from the connection pool
        connection, cursor = connect_to_db()

        if connection is None or cursor is None:
            raise Exception("Failed to connect to the database")

        # Execute SQL query to insert admin data
        sql_query = """
            INSERT INTO admins (username, emailid, password)
            VALUES (%s, %s, %s)
            RETURNING id, username, emailid
        """
        cursor.execute(sql_query, (username, email, password))
        new_admin = cursor.fetchone()

        # Commit the transaction
        connection.commit()

        # Close the cursor and connection
        cursor.close()
        connection.close()

        # Prepare the response data
        admin_details = {
            'id': new_admin[0],
            'username': new_admin[1],
            'email': new_admin[2]
        }

        return jsonify(admin_details)

    except (Exception, psycopg2.Error) as error:
        print("Error:", error)
        return jsonify({'error': 'Failed to add admin'}), 500

# Route to get all admins
@app.route('/getadmins', methods=['GET'])
def get_admins():
    try:
        # Get a connection from the connection pool
        connection, cursor = connect_to_db()

        if connection is None or cursor is None:
            raise Exception("Failed to connect to the database")

        # Execute SQL query to fetch all admins
        cursor.execute("SELECT id, username, emailid FROM admins")
        admins = cursor.fetchall()

        # Close the cursor and connection
        cursor.close()
        connection.close()

        # Prepare the response data
        admins_data = []
        for admin in admins:
            admin_details = {
                'id': admin[0],
                'username': admin[1],
                'email': admin[2]
            }
            admins_data.append(admin_details)

        return jsonify(admins_data)

    except (Exception, psycopg2.Error) as error:
        print("Error:", error)
        return jsonify({'error': 'Failed to retrieve admins'}), 500

# Route to delete an admin by id
@app.route('/deleteadmin/<int:admin_id>', methods=['DELETE'])
def delete_admin(admin_id):
    try:
        # Get a connection from the connection pool
        connection, cursor = connect_to_db()

        if connection is None or cursor is None:
            raise Exception("Failed to connect to the database")

        # Execute SQL query to delete the admin
        cursor.execute("DELETE FROM admins WHERE id = %s", (admin_id,))

        # Commit the transaction
        connection.commit()

        # Close the cursor and connection
        cursor.close()
        connection.close()

        return jsonify({'message': 'Admin deleted successfully'})

    except (Exception, psycopg2.Error) as error:
        print("Error:", error)
        return jsonify({'error': 'Failed to delete admin'}), 500

@app.route('/getadmin', methods=['GET'])
def get_admin_by_email_password():
    try:
        # Get email and password from the request
        email = request.args.get('email')
        password = request.args.get('password')

        # Get a connection from the connection pool
        connection, cursor = connect_to_db()

        if connection is None or cursor is None:
            raise Exception("Failed to connect to the database")

        # Execute SQL query to fetch the admin by email and password
        cursor.execute("SELECT id, username, emailid FROM admins WHERE emailid = %s AND password = %s", (email, password))
        admin = cursor.fetchone()

        # Close the cursor and connection
        cursor.close()
        connection.close()

        # Check if admin exists
        if admin:
            admin_details = {
                'id': admin[0],
                'username': admin[1],
                'email': admin[2]
            }
            return jsonify(admin_details)
        else:
            return jsonify({'error': 'Admin not found'}), 404

    except (Exception, psycopg2.Error) as error:
        print("Error:", error)
        return jsonify({'error': 'Failed to retrieve admin'}), 500
 
@app.route('/deletequestion/<int:question_id>', methods=['DELETE'])
def delete_question(question_id):
    try:
        connection, cursor = connect_to_db()

        # Check if the question exists
        cursor.execute("SELECT * FROM questions WHERE id = %s", (question_id,))
        question = cursor.fetchone()
        if question is None:
            return jsonify({'status': 'fail', 'message': 'Question not found'}), 404

        # Delete associated comments
        cursor.execute("DELETE FROM comments WHERE question_id = %s", (question_id,))

        # Delere associated likes
        cursor.execute("DELETE FROM question_likes WHERE question_id = %s", (question_id,))

        # Delete the question
        cursor.execute("DELETE FROM questions WHERE id = %s", (question_id,))

        # Commit the transaction
        connection.commit()

        # Close cursor and connection
        cursor.close()
        connection.close()

        return jsonify({'status': 'success', 'message': 'Question and associated comments deleted successfully'}), 200
    except (Exception, psycopg2.Error) as error:
        print("Error:", error)
        return jsonify({'status': 'error', 'message': 'Internal server error'}), 500

@app.route('/deletecomment/<int:comment_id>', methods=['DELETE'])
def delete_comment(comment_id):
    try:
        connection, cursor = connect_to_db()

        # Check if the comment exists
        cursor.execute("SELECT * FROM comments WHERE id = %s", (comment_id,))
        comment = cursor.fetchone()
        if comment is None:
            return jsonify({'status': 'fail', 'message': 'Comment not found'}), 404

        # Delete the comment
        cursor.execute("DELETE FROM comments WHERE id = %s", (comment_id,))

        # Commit the transaction
        connection.commit()

        # Close cursor and connection
        cursor.close()
        connection.close()

        return jsonify({'status': 'success', 'message': 'Comment deleted successfully'}), 200
    except (Exception, psycopg2.Error) as error:
        print("Error:", error)
        return jsonify({'status': 'error', 'message': 'Internal server error'}), 500
    

@app.route('/deleteblog/<int:blog_id>', methods=['DELETE'])
def delete_blog(blog_id):
    try:
        connection, cursor = connect_to_db()

        # Check if the comment exists
        cursor.execute("SELECT * FROM blogs WHERE id = %s", (blog_id,))
        comment = cursor.fetchone()
        if comment is None:
            return jsonify({'status': 'fail', 'message': 'Blog not found'}), 404

        # Delete the comment
        cursor.execute("DELETE FROM blogs WHERE id = %s", (blog_id,))

        # Commit the transaction
        connection.commit()

        # Close cursor and connection
        cursor.close()
        connection.close()

        return jsonify({'status': 'success', 'message': 'Blog deleted successfully'}), 200
    except (Exception, psycopg2.Error) as error:
        print("Error:", error)
        return jsonify({'status': 'error', 'message': 'Internal server error'}), 500

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5001)

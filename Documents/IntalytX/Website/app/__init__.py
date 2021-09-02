
from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from flask_login import LoginManager, UserMixin, login_required, login_user, logout_user
from datetime import datetime
from werkzeug.security import generate_password_hash, check_password_hash

from os import environ


app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = environ.get('DATABASE_URL') or 'sqlite:///myDB.db'

app.config['SECRET_KEY'] =  environ.get('SECRET_KEY') 
db = SQLAlchemy(app)

from app import routes
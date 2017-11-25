import os
import uuid
import sqlite3
from flask import Flask, request, redirect, url_for
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy.ext.declarative import declarative_base
from werkzeug.utils import secure_filename

app = Flask(__name__)

Base = declarative_base()

UPLOAD_FOLDER = './uploads'
ALLOWED_EXTENSIONS = set(['txt', 'pdf', 'png', 'jpg', 'jpeg', 'gif'])

app = Flask(__name__)
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:////tmp/test.db'
db = SQLAlchemy(app)

challenge_tag = db.Table('challenge_tag', db.Model.metadata,
    db.Column('challenge_id', db.Integer, db.ForeignKey('challenge.id')),
    db.Column('tag_id', db.Integer, db.ForeignKey('tag.id'))
)

class Tag(db.Model):
    __tablename__ = "tag"
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(80), unique=True)

    def __repr__(self):
        return '<Tag name={}>'.format(self.name)

class Challenge(db.Model):
    __tablename__ = "challenge"
    id = db.Column(db.Integer, primary_key=True)
    picture = db.Column(db.String(256), unique=True)
    tags = db.relationship("Tag",
        secondary=challenge_tag)

    def __repr__(self):
        return '<Challenge {}>'.format(self.tags)

def get_or_create(model, defaults=None, **kwargs):
    instance = db.session.query(model).filter_by(**kwargs).first()
    if instance:
        return instance, False
    else:
        params = dict((k, v) for k, v in kwargs.iteritems() if not isinstance(v, ClauseElement))
        params.update(defaults or {})
        instance = model(**params)
        db.session.add(instance)
        return instance, True

@app.route("/")
def hello():
    return "Hello World!"

def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS

@app.route("/upload", methods=['GET', 'POST'])
def upload_challenge():
    if request.method == 'POST':
        arg_tags = request.form.get('tags', None).split(',')
        # all_tags = request.data.get('all_tags', None)
        if 'file' not in request.files or not arg_tags:
            return 'missing data/arguments', 400 # oops
        file = request.files['file']

        if file.filename == '':
            # flash('No selected file')
            return redirect(request.url)
        filename = str(uuid.uuid4())
        if file:
            filename = secure_filename(filename)
            chal = Challenge(picture=filename)
            for arg_tag in arg_tags:
                tag, _ = get_or_create(Tag, name=arg_tag)
                chal.tags.append(tag)

            db.session.add(chal)
            db.session.commit()

            file.save(os.path.join(app.config['UPLOAD_FOLDER'], filename))

    return '''
    <!doctype html>
    <title>Upload new File</title>
    <h1>Upload new File</h1>
    <form method=post enctype=multipart/form-data>
      <p><input type=file name=file>
         <input type=submit value=Upload>
         <input type="hidden" name="tags" value="abc,de">
    </form>
    '''



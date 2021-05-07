from flask import Flask, request
import json

app = Flask(__name__)


@app.route('/')
def hello_world():
    return 'Hello World!'


@app.route('/a')
def a():
    page = request.args.get('page', default=0, type=int)
    filter = request.args.get('filter', default='*', type=str)
    RE = 'page: ' + str(page) + '\n' + 'filter: ' + str(filter)
    return RE


@app.route('/add', methods=['POST'])
def add():
    f = request.json()
    return f


if __name__ == '__main__':
    app.run(host='192.168.1.44', port=443)

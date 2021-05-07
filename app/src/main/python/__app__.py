from flask import Flask, request
from github import Github
import json

app = Flask(__name__)


@app.route('/<link>')
def get_file(link):
    print(link)
    print(link.replace("#", "/"))
    return python_parser(link.replace("Ю", "/"))


class aye:
    def __init__(self):
        self.g = Github("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")
        self.user = self.g.get_user_by_id(57916666)

    def getProjects(self, name, repo, path):
        print()
        print(f"{name}/{repo}")
        repo = self.g.get_repo(f"{name}/{repo}")
        print(repo)
        file_content = repo.get_contents(path)
        return file_content.decoded_content.decode()
        # for content_file in contents:
        #     print(content_file.decoded_content)


def python_parser(link):
    print("\t\t", link)
    name, repo, path = link.split("/")[0], link.split("/")[1], "/".join(link.split("/")[2:])
    print(name, repo, path)
    a = aye()
    code = str(a.getProjects(name, repo, path)).split("\n")
    structure = {}
    independent = []
    for i in code:
        if i.split() and i.split()[0] in ['class', 'def']:
            t_count = i.count("    ")
            if t_count == 0:
                structure[i] = []
                independent = i
            else:
                structure[independent].append(i.lstrip())
    # print(structure)
    return dictToJson(structure)


def dictToJson(dictionary):
    json_dictionary = json.dumps(dictionary)
    return json_dictionary


if __name__ == '__main__':
    app.run(host='192.168.1.44', port=443)
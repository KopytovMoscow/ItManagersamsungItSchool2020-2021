from github import Github
import json


class aye:
    def __init__(self):
        self.g = Github("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")
        self.user = self.g.get_user_by_id(57916666)

    def getProjects(self, link):
        # print("\t", link.decode("utf-8"))
        for i in self.user.get_repos():
            print(i.name, end=" ")
        print()
        repo = self.g.get_repo(link)
        contents = repo.get_contents("")
        file_content = repo.get_contents('main.py')
        return file_content.decoded_content.decode()
        # for content_file in contents:
        #     print(content_file.decoded_content)


def python_parser(link):
    a = aye()
    code = str(a.getProjects(link)).split("\n")
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


print(python_parser("KopytovMoscow/Task2/blob/master/main.py"))

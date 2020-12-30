import requests
import codecs
import atoma
from flask import Flask
from flask import request
from flask import jsonify
from crawling import Crawler


app = Flask(__name__)

@app.route('/', methods=['POST'])
def parse_calander():
    print(request.json)
    json = request.json
    user_id = json["user_id"]
    user_pw = json["user_pw"]
    print(user_id, type(user_pw))
    crawler = Crawler(user_id, user_pw)
    url = crawler.run()

    response = requests.get(url)

    f = atoma.parse_atom_bytes(response.content)
    #f = atoma.parse_atom_file('./calander/result.atom')
    calander_info = []

    for entry in f.entries:
        data = {}
        report_name = entry.title.value    # 과제 명
        course_name = entry.authors[0].name    # 수업 명
        deadline = str(entry.content.value[:entry.content.value.find("<")]) # 제출 기한
        dead_month = deadline[deadline.find("마감일: ")+5 : deadline.find("월")]
        dead_day = deadline[deadline.find("월")+2 : deadline.find("일", deadline.find("월")+2)]

        if deadline.find("오후") != -1:
            dead_hour = str(int(deadline[deadline.find("오후")+3 : deadline.find(":", deadline.find("오후")+3)])+12)
            dead_min = deadline[deadline.find(":", deadline.find("오후")+4)+1 : deadline.find("까지")]
        else:
            dead_hour = deadline[deadline.find("오전")+3 : deadline.find(":", deadline.find("오전")+3)]
            dead_min = deadline[deadline.find(":", deadline.find("오전")+4)+1 : deadline.find("까지")]
        href = entry.links[0].href

        data['report_name'] = report_name
        data['course_name'] = course_name
        data['dead_month'] = dead_month
        data['dead_day'] = dead_day
        data['dead_hour'] = str(int(dead_hour))
        data['dead_min'] = dead_min
        data['href'] = href
        

        calander_info.append(data)
        response = {"calander_info": calander_info}



if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=True)
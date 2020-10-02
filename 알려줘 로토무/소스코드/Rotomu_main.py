import re
import json
import urllib.request
import urllib.parse
from bs4 import BeautifulSoup
from flask import Flask, request
from slack import WebClient
from slackeventsapi import SlackEventAdapter
from slack.web.classes import extract_json
from slack.web.classes.blocks import *
from slack.web.classes.elements import *
from slack.web.classes.interactions import MessageInteractiveEvent
from poke_data import poke_data


SLACK_TOKEN = 
SLACK_SIGNING_SECRET =
app = Flask(__name__)
slack_events_adaptor = SlackEventAdapter(SLACK_SIGNING_SECRET, "/re", app)
slack_web_client = WebClient(token=SLACK_TOKEN)


# url 만드는 함수
def _url(text):

    print(url_text)
    url_text = urllib.parse.quote_plus(url_text)
    # print('encoding: ' + url_text)
    url_pokename = url_text.replace("%5C", "%")
    # print('replace: ' + url_pokename)
    url_tmp = 'http://pokemon.fandom.com/ko/wiki/'
    url = url_tmp + url_pokename
    # print('최종 URL: ' + url)

    return url


# 메가진화가 존재하는 포켓몬, 크롤링 인덱스 구하는 함수
def _detect(div_tag):
    text_list = []
    for table_tag in div_tag.find_all('table'):
        text_list.append(table_tag.get_text().split())

    mega = False
    abil_index = 0

    #오류 발생 시 긴급 예외처리
    try:
        for i in text_list:
            if '메가진화' in i:
                mega = True
            if 'HP' in i[0]:
                abil_index = text_list.index(i)
                break
        return [mega, abil_index, text_list]
    except:
        return [False, 0, ['`X`']*len(text_list)]




# 포켓몬 생태 크롤링
def _crawl_ecology(div_tag):
    eco_list = []
    for eco in div_tag.find_all('p'):
        eco_list.append(eco.get_text())

    if eco_list[1] == '\n':
        return '_없음_'
    else:
        return eco_list[1]


# 포켓몬 종족값 크롤링
def _crawl_ability(_detect):

    mega = _detect[0]
    abil_index = _detect[1]
    text_list = _detect[2]

    abil_dic = {'HP' : 0, '공격' : 0, '방어' : 0, "특수공격" : 0, '특수방어' : 0, '스피드' : 0, '총합' : 0}
    mega_abil_dic = {'HP' : 0, '공격' : 0, '방어' : 0, "특수공격" : 0, '특수방어' : 0, '스피드' : 0, '총합' : 0}

    message = ''

    if mega:
        abil_dic['HP'] = text_list[abil_index][1]
        abil_dic['공격'] = text_list[abil_index + 1][1]
        abil_dic['방어'] = text_list[abil_index + 2][1]
        abil_dic['특수공격'] = text_list[abil_index + 3][1]
        abil_dic['특수방어'] = text_list[abil_index + 4][1]
        abil_dic['스피드'] = text_list[abil_index + 5][1]
        abil_dic['총합'] = text_list[abil_index + 6][1]

        mega_abil_dic['HP'] = text_list[abil_index + 8][1]
        mega_abil_dic['공격'] = text_list[abil_index + 9][1]
        mega_abil_dic['방어'] = text_list[abil_index + 10][1]
        mega_abil_dic['특수공격'] = text_list[abil_index + 11][1]
        mega_abil_dic['특수방어'] = text_list[abil_index + 12][1]
        mega_abil_dic['스피드'] = text_list[abil_index + 13][1]
        mega_abil_dic['총합'] = text_list[abil_index + 14][1]


        for key, val in abil_dic.items():
            message += key + ' : '
            message += '_' + str(val) + '_\n'

        message += '\n*메가 진화 시*\n'
        for key, val in mega_abil_dic.items():
            message += key + ' : '
            message += '_' + str(val) + '_\n'


    else:
        abil_dic['HP'] = text_list[abil_index][1]
        abil_dic['공격'] = text_list[abil_index + 1][1]
        abil_dic['방어'] = text_list[abil_index + 2][1]
        abil_dic['특수공격'] = text_list[abil_index + 3][1]
        abil_dic['특수방어'] = text_list[abil_index + 4][1]
        abil_dic['스피드'] = text_list[abil_index + 5][1]
        abil_dic['총합'] = text_list[abil_index + 6][1]

        for key, val in abil_dic.items():
            message += key + ' : '
            message += '_' + str(val) + '_\n'

    return message


# 포켓몬 상성 크롤링
def _crawl_rivality(div_tag):
    text_list = []
    for step0 in div_tag.find_all('table', class_='roundy'):
        text_list.append(step0.get_text().split())

    # rivl_dic = {'노말':'', '격투':'', '비행':'', '독':'', '땅':'', '바위':'', '벌레':'', '고스트':'', '강철':'',
    #             '불꽃':'', '물':'', '풀':'', '전기':'', '에스퍼':'', '얼음':'', '드래곤':'', '악':'', '페어리':''}

    rivl_dic = {}
    for i in text_list:
        if '보통' in i[0]:
            temp_list = i[2:]
            # print(temp_list)
            # print("-----------------------------------")
            for j in range(0,36,2):
                rivl_dic[temp_list[j]] = temp_list[j+1]
            break
    # print(rivl_dic)

    good_efct = []
    bad_efct = []
    no_efct = []

    for typ, effect in rivl_dic.items():
        if effect == '4×'or effect =='2×':
            good_efct.append('`' + typ + '`')
        elif effect == '0.5×'or effect == '0.25×':
            bad_efct.append('`' + typ + '`')
        elif effect == '0×':
            no_efct.append('`' + typ + '`')



    message_list = ['*:star-struck: 효과가 좋다     :* ',
                    '*:thinking_face: 효과가 별로다  :* ',
                    '*:sob: 효과가 없다     :* ']


    message_list[0] += ', '.join(good_efct)
    message_list[1] += ', '.join(bad_efct)
    message_list[2] += ', '.join(no_efct)

    return '\n'.join(message_list)


# 포켓몬 외국명칭, 타입 크롤링
def _crawl_pokemon_keywords(soup, text):
    result = []

    # 외국명칭
    name_ja = ''
    count = 0
    for tag in soup.find_all('div', class_='infobox-pokemon'):

        for name_tag in tag('div', class_='name-ja'):
            count += 1

            text_name = name_tag.get_text()

            name_ja += "[" + text_name + "]" + ' '
            
            if count >= 2:
                if count % 2 == 0:
                    name_ja += '\n'

    # print('외국명칭: ' + name_ja)
    result.append(name_ja + '\n')


    # 포켓몬 타입
    type_poke = ''
    for tag in soup.find_all('div', class_='infobox-pokemon'):
        for type_tag in tag.find_all('span', class_='split-cell'):
            text_type = type_tag.get_text()
            type_poke += "`" + text_type + "`" + ' '
    # print('타입: ' + type_poke)
    result.append('*타입:* ' + type_poke)

    return '\n'.join(result)


# 포켓몬 특성 크롤링
def _crawl_pokemon_character(soup, text):
    result = []

    # 포켓몬 특성
    character = []
    for tag in soup.find_all('div', class_='infobox-pokemon'):
        for charac_tag in tag.find_all('span', class_='ajaxttlink'):
            text_charac = charac_tag.get_text()
            character.append(text_charac)

        # print('특성: ' + str(character[: len(character) - 1]))
        # print('숨겨진 특성: ' + str(character[len(character) - 1:]))

    char_ = character[: len(character) - 1]
    char_ = list(map(lambda x: '_' + x + '_', char_))
    char_ = ', '.join(char_)

    hidden_char_ = '_' + character[len(character) - 1:][0] + '_'

        # print(char_)
        # print(hidden_char_)

    result.append('*특성:* ' + char_)
    result.append('*숨겨진 특성:* ' + hidden_char_)


    result = list(map(lambda x: x.replace("'", ''), result))

    return '\n'.join(result)


# 이미지 url 크롤링
def _image_url(soup):
    for tag in soup.find_all('div', class_='image'):
        for img_tag in tag.find_all('img'):
            img_url = img_tag['src']
            # print('이미지 주소: ' + img_url)

    return img_url


# DB에서 포켓몬 존재 유무 확인
def _isPoke(text):
    for no, name in poke_data.items():

        if no == text:
            # print(no, name)
            return True

        if name == text:
            # print(no, name)
            return True

    return False


# DB에서 포켓몬 찾기
def _search_poke_data(text):

    for no, name in poke_data.items():
        if no == text:
            # message = '`No.{0}`\t*{1}*'.format(no, name)
            return no, name

        elif name == text:
            # message = '`No.{0}`\t*{1}*'.format(no, name)
            return no, name


# 버튼 링크
def poke_character_link(text):
    url_text = text
    print(url_text)

    url_text = urllib.parse.quote_plus(url_text)
    print('encoding: ' + url_text)

    url_pokename = url_text.replace("%5C", "%")
    print('replace: ' + url_pokename)

    url_tmp = 'http://pokemon.fandom.com/ko/wiki/'
    url = url_tmp + url_pokename

    print('최종 URL: ' + url)

    source_code = urllib.request.urlopen(url).read()
    soup = BeautifulSoup(source_code, "html.parser")

    charac_prt = ''
    for tag in soup.find_all('table', class_='float-right'):
        for charac_tag in tag.find_all('td', colspan='2', class_='bg-white'):
            charac_text = charac_tag.get_text().strip().replace('\n', '')
            charac_prt += charac_text
    print(charac_prt)

    return '`'+text+'`' + ' ' + charac_prt


# 버튼
@app.route("/click", methods=["GET", "POST"])
def on_button_click():
    # print('버튼눌림')
    # 버튼 클릭은 SlackEventsApi에서 처리해주지 않으므로 직접 처리합니다
    payload = request.values["payload"]
    click_event = MessageInteractiveEvent(json.loads(payload))
    text = click_event.value
    # print(text)
    # 다른 가격대로 다시 크롤링합니다.
    message_blocks = poke_character_link(text)
    # print('여기까지 성공')
    # 메시지를 채널에 올립니다
    slack_web_client.chat_postMessage(
        channel=click_event.channel.id,
        text=message_blocks
    )
    # Slack에게 클릭 이벤트를 확인했다고 알려줍니다
    return "OK", 200

def _button(soup, text):
    character = []
    for tag in soup.find_all('div', class_='infobox-pokemon'):
        for charac_tag in tag.find_all('span', class_='ajaxttlink'):
            text_charac = charac_tag.get_text()
            character.append(text_charac)

    if len(character) == 1:
        charac_1 = character[0]
        hide_charac = '숨겨진 특성이 없습니다.'
        button_actions = ActionsBlock(
            block_id=text,
            elements=[
                ButtonElement(
                    text=charac_1,
                    action_id="price_up_1", value=charac_1
                ),
                ButtonElement(
                    text=hide_charac, style="danger",
                    action_id="price_up_5", value=hide_charac
                ),
            ]
        )

    elif len(character) == 2:
        charac_1 = character[0]
        hide_charac = character[1]
        button_actions = ActionsBlock(
            block_id=text,
            elements=[
                ButtonElement(
                    text=charac_1,
                    action_id="price_up_1", value=charac_1
                ),
                ButtonElement(
                    text=hide_charac, style="danger",
                    action_id="price_up_5", value=hide_charac
                ),
            ]
        )

    elif len(character) == 3:
        charac_1 = character[0]
        charac_2 = character[1]
        hide_charac = character[2]
        button_actions = ActionsBlock(
            block_id=text,
            elements=[
                ButtonElement(
                    text=charac_1,
                    action_id="price_up_1", value=charac_1
                ),
                ButtonElement(
                    text=charac_2,
                    action_id="price_up_5", value=charac_2
                ),
                ButtonElement(
                    text=hide_charac, style="danger",
                    action_id="price_down_1", value=hide_charac
                ),
            ]
        )

    else:
        button_actions = ActionsBlock(
            block_id=text,
            elements=[
                ButtonElement(
                    text="error",
                    action_id="price_up_1", value=str(10000)
                ),
            ]

        )

    # print(text)

    return button_actions


# 챗봇이 멘션을 받았을 경우
@slack_events_adaptor.on("app_mention")
def app_mentioned(event_data):
    channel = event_data["event"]["channel"]
    text = ''

    text = event_data["event"]["text"]
    text = text[13:]
    text.strip()

    if _isPoke(text):
        my_blocks = []

        poke_no = _search_poke_data(text)[0]
        poke_name = _search_poke_data(text)[1]

        url = _url(poke_name)
        source_code = urllib.request.urlopen(url).read()
        soup = BeautifulSoup(source_code, 'html.parser')
        div_tag = soup.find('div', class_='mw-content-ltr mw-content-text')

        url_block = SectionBlock(
            text='<'+ url +'>'
        )

        img_block = ImageBlock(
            image_url=_image_url(soup),
            alt_text='_*No image*_'
        )

        message = '`No.{0}` \n *{1}* \n'.format(poke_no, poke_name)
        message += _crawl_pokemon_keywords(soup, text) + '\n\n'
        # message += _crawl_pokemon_character(soup, text) + '\n\n\n'
        message += '*특성*'

        info_block1 = SectionBlock(
            text=message
        )

        message = ''
        message += '*생태*\n'
        message += _crawl_ecology(div_tag) + '\n\n\n'
        message += _crawl_rivality(div_tag) + '\n\n\n'
        message += '*종족값*\n'
        message += _crawl_ability(_detect(div_tag))


        info_block2 = SectionBlock(
            text=message
        )


        my_blocks = [url_block, img_block, info_block1, _button(soup, text), info_block2]
        slack_web_client.chat_postMessage(
            channel=channel,
            blocks=extract_json(my_blocks)
        )

    else:
        if text == '':
            message = '나랑 대화하기 싫은거야?? :disappointed:'
        else:
            message = '응~~ 없어~~~~ :stuck_out_tongue_winking_eye:'
        message += '\n\n_Caution_\n'
        message += '포켓몬 ' + '*이름* ' + '혹은 ' + '`도감번호` ' + '를 입력해주세요.'

        slack_web_client.chat_postMessage(
            channel=channel,
            text=message
        )


# / 로 접속하면 서버가 준비되었다고 알려줍니다.
@app.route("/", methods=["GET"])
def index():
    return "<h1>Server is ready.</h1>"


if __name__ == '__main__':
    app.run('127.0.0.1', port=8080)

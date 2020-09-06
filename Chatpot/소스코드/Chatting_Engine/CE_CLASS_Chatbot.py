import random
import time
from CE_DATA_Chatbot import *
from CE_CLASS_PlantFeeling import Feeling
from CE_CLASS_Crawling import Crawling

class Response:
    def chatbot(self, user_message, plant_info):
        feeling = Feeling(plant_info)
        bot_message = ''
        info_dic = {'key':{'conv':[], 'data':[], 'feel':[], 'craw':[]},
                    'num':[0,0,0,0],
                    'msg':['','','',''],
                    'hert':int(plant_info['Heart']),
                    'cond':feeling.feeling,
                    'detl':feeling.detail
                    }

        self.find_keywords(user_message, info_dic)

        bot_message = self.package_message(info_dic, plant_info)

        return bot_message

    def find_keywords(self, user_msg, info_dic):

        for key1 in conv_DB:
            for key2 in conv_DB[key1]['user']:
                if key2 in user_msg:
                    if key1 in info_dic['key']['conv']:
                        pass
                    else:
                        info_dic['key']['conv'].append(key1)
                        info_dic['num'][0] += 1

        for key1 in data_DB:
            for key2 in data_DB[key1]:
                if key2 in user_msg:
                    if key1 in info_dic['key']['data']:
                        pass
                    else:
                        info_dic['key']['data'].append(key1)
                        info_dic['num'][1] += 1

        for key in feel_DB['Feel']['user']:
            if key in user_msg:
                if 'Feel' in info_dic['key']['feel']:
                    pass
                else:
                    info_dic['key']['feel'].append('Feel')
                    info_dic['num'][2] += 1

        for key1 in craw_DB:
            for key2 in craw_DB[key1]:
                if key2 in user_msg:
                    if key1 in info_dic['key']['data']:
                        pass
                    else:
                        info_dic['key']['data'].append(key1)
                        info_dic['num'][3] += 1

    def package_message(self, info_dic, plant_info):
        message = ''

        if info_dic['num'][0] > 0:
            info_dic['msg'][0] = self.handle_conv(info_dic)
        if info_dic['num'][1] > 0:
            info_dic['msg'][1] = self.handle_data(info_dic, plant_info)
        if info_dic['num'][2] > 0:
            info_dic['msg'][2] = self.handle_feel(info_dic)
        if info_dic['num'][3] > 0:
            info_dic['msg'][3] = self.handle_craw(info_dic)

        if sum(info_dic['num']) == 1:
            for i in range(len(info_dic['num'])):
                if info_dic['num'][i] == 1:
                    message += info_dic['msg'][i]
                    break

        elif sum(info_dic['num']) == 0:
            message = 'I\'m so sorry, but I love you Da Geojitmal (by Developer)'
            return message

        elif sum(info_dic['num']) > 1:
            for i in range(len(info_dic['num'])):
                if info_dic['num'][i] >= 1:
                    if message is not '':
                        message += '\n'
                    message += info_dic['msg'][i]
        return message

    def handle_conv(self, info_dic):
        conv_msg = ''
        for key in info_dic['key']['conv']:
            rand = random.randrange(len(conv_DB[key]['bot'][info_dic['hert']]))
            conv_msg += conv_DB[key]['bot'][info_dic['hert']][rand]
        #conv_msg += self.append_emoticon(info_dic)
        return conv_msg

    def handle_data(self, info_dic, plant_info):
        data_msg = ''
        for key in info_dic['key']['data']:
            data_msg += ('{0} is {1}{2}'.format(data_DB[key][0], str(round(float(plant_info[key],2))), data_DB['Unit'][key]))
            if key != info_dic['key']['data'][len(info_dic['key']['data'])-1]:
                data_msg += ' and '
        data_msg += self.append_emoticon(info_dic)
        return data_msg

    def handle_feel(self, info_dic):
        feel_msg   = ''
        detail_msg = ''
        condition = info_dic['cond']
        detail    = info_dic['detl']
        feel_db_feel_bot = feel_DB['Feel']['bot']
        feel_db_datail   = feel_DB['Detail']

        feel_msg += feel_db_feel_bot[condition][self.rand(feel_db_feel_bot[condition])]

        for key in detail:
            if key is 'Temp':
                if detail[key] is not 'good' and detail[key] is not 'great':
                    if condition > 1:
                        detail_msg += 'But'
                    else:
                        detail_msg += 'Beacuase '
                    detail_msg += feel_db_datail[key][detail[key]][self.rand(feel_db_datail[key][detail[key]])]
                    feel_msg += '\n' + detail_msg
            else:
                if detail[key] is not 'good':
                    if detail_msg is not '':
                        detail_msg += ' and '
                    else:
                        if condition > 1:
                            detail_msg += 'But '
                        else:
                            detail_msg += 'Beacuase '
                    detail_msg += feel_db_datail[key][detail[key]][self.rand(feel_db_datail[key][detail[key]])]
                    feel_msg += '\n' + detail_msg
        feel_msg += self.append_emoticon(info_dic)
        return feel_msg

    def handle_craw(self, info_dic):
        craw = Crawling()
        craw_msg = craw.weather()
        #time.sleep(0.5)
        return craw_msg

    def rand(self, list):
        randnum = random.randrange(len(list))
        return randnum

    def append_emoticon(self, info_dic):
        heart = info_dic['hert']
        condition = info_dic['cond']
        emoticon_db = feel_DB['Emoticon']
        emoticon = ''
        if heart is not 1:
            emoticon = ' ' + emoticon_db[condition][heart][self.rand(emoticon_db[condition][heart])]
        return emoticon

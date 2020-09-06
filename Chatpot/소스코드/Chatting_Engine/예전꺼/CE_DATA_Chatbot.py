#from DATA_conv import *
import random

conv_start_DB = ['It\'s ', 'I\'m' ]
intensity_DB = {'strong':['very', 'really'],
                'weak':['little']}
###########################################################크롤링관련
craw_DB = {
'Weather':['Weather', 'weather', ]
}


###########################################################센서관련
data_DB = {

#이름
'Name':['My Name', 'Name', 'name'],

#종
'Species':['Species', 'species', 'Kind', 'kind'],

#온도
'Temp':['Temperature','temperature','Temp','temp'],

#습도
'Humi':['Humid','humid','Humi','humi'],

#단위
'Unit':{ 'Name':'', 'Species':'', 'Temp':'\'c', 'Humi':'%'}
}

###########################################################기분관련
feel_DB = {
    'Feel':{
        'user':['Feel', 'feel', 'How is it going', 'how is it going', 'How have you been', 'how have you been', 'How about you',
                'how about you', 'How are you', 'how are you','What happen?', 'what happen?','What happen to you', 'what happen to you'],
        'bot':{0:['I\'m really bad', 'Very Bad', 'I feel very bad'],
               #'very bad':['I\'m really {rand}'format('Bad')],
               1:['I\'m bad', 'I\'m not good', 'Little bad', 'Not good'],
               2:['I\'m fine', 'Fine', 'Fine, thanks'],
               3:['I\'m good', 'Better', 'Not bad', 'Quite good'],
               4:['I\'m really good', 'Very good', 'I feel very good', 'I\'m really happy', 'Nothing is better', 'Nothing better than now']}},

    'Detail':{
        'Temp':{
        'cold':['It\'s Chilly', 'It\'s cold'],
        'very cold':['I\'m very cold', 'I\'m really cold', 'It\'s so cold'],
        'freeze':['I\'m freezing now...', 'Quite freeze...', 'I\'ll be dead...'],
        'hot':['It\'s hot'],
        'very hot':['It\'s really hot', 'It\'s very hot', 'It\'s quite hot', 'I\'m so hot', 'I\'m very hot', 'I\'m really hot'],
        'burn':['I\'m burning now...', 'I\'ll be burned', 'I\'ll be dead...']},

        'Grou':{
        'little':['I\'m little thirsty'],
        'scant':['It\'s terribly thirsty', 'I\'m really thirsty', 'I want water right now'],
        'much':['I\'m little full'],
        'flood':['Water is too much..', 'I\'ll be swept away...']},

        'Ligh':{
        'dark':['It\'s dark', 'Light is weak'],
        'blinding':['It\'s dazzling', 'Light is strong']}},

    'Emoticon':{
        4:{2:[':)', '^^'],
           3:[':)', '^^', '> <', ':D', '^o^', '>o<']},
        3:{2:[':)', '^^'],
           3:[':)', '^^', ':D', '^o^']},
        2:{2:[':)'],
           3:[':)', '^^']},
        1:{2:[':(', '-_-'],
           3:[':(', ';(', 'T T']},
        0:{2:[':(', ';(', '-_-'],
           3:['8^8', 'T T', 'ToT', 'T-T']}}
}

###########################################################기본적인 대화
conv_DB = {
#의문
    'What':{
        'user':['What', 'what'],
        'bot':{1:['What?'],
               2:['Parden?'],
               3:['?']}},

#웃음
    'Laugh':{
        'user':['ha ha ha', 'haha', 'Haha', 'kkk', 'hhh'],
        'bot':{1:['Why are you laughing?'],
               2:['Ha ha ha'],
               3:['kkkkkkkk', 'hhhhhhhh']}},
#안부
    'Action':{
        'user':['What are you doing', 'what are you doing'],
        'bot':{1:['Nothing to do'],
               2:['Well.. I don\'t know'],
               3:['I\'m watching you', 'I\'m chatting with you']}},

#감사
    'Thank':{
        'user':['Thank', 'thank'],
        'bot':{1:['Your welcome'],
               2:['No problem'],
               3:['Yap']}},

#인사
    'Greeting':{
        'user':['Hi', 'Hello', 'hi', 'hello', 'Hey', 'hey'],
        'bot':{1:['Nice to meet you'],
               2:['Hello'],
               3:['Hi', 'Hey']}},
    'Greeting2':{
        'user':['What\'s up', 'what\'s up'],
        'bot':{1:['Not too much'],
               2:['Nothing much', 'Not too much'],
               3:['Hey, what\'s up', 'Not much, how about you?']}},

#사과
    'Sorry':{
        'user':['sorry', 'my bad'],
        'bot':{1:['...'],
               2:['...'],
               3:['That\'s okay']}},

#인물
    'Ju Sanghoon':{
        'user':['sanghoon', 'sang hoon', 'Sanghoon', 'Sang Hoon', 'jusanghoon', 'ju sanghoon'],
        'bot':{1:['Android developer of Chatpot, if you find problem in application, you should to beat sb up him','Hulk Buster', 'Hulk', 'Hongik Ingan', 'Gaekangpae'],
               2:['Android developer of Chatpot, if you find problem in application, you should to beat sb up him','Hulk Buster', 'Hulk', 'Hongik Ingan', 'Gaekangpae'],
               3:['Android developer of Chatpot, if you find problem in application, you should to beat sb up him','Hulk Buster', 'Hulk', 'Hongik Ingan', 'Gaekangpae']}},

    'Lee haeju':{
        'user':['hae ju', 'Hae Ju', 'haeju', 'Haeju', 'leehaeju', 'lee haeju'],
        'bot':{1:['Server and Database developer of Chatpot, if you find problem in server and DB, you should to beat sb up him', 'Haejjujju', 'Chiup kangpae', 'German', 'Dog rich'],
               2:['Server and Database developer of Chatpot, if you find problem in server and DB, you should to beat sb up him', 'Haejjujju', 'Chiup kangpae', 'German', 'Dog rich'],
               3:['Server and Database developer of Chatpot, if you find problem in server and DB, you should to beat sb up him', 'Haejjujju', 'Chiup kangpae', 'German', 'Dog rich']}},

    'Jeon seungyong':{
        'user':['seungyong', 'seung yong', 'jeon seungyong', 'jeonseungyong'],
        'bot':{1:['Device software developer of Chatpot, if you find operate problem in device, you should to beat sb up him', 'Automobile', 'Car', 'Ah jib e gagoshipda', 'I want go home', 'Sanghoony hyung', 'Hyung jeo jom dowajoyo'],
               2:['Device software developer of Chatpot, if you find operate problem in device, you should to beat sb up him', 'Automobile', 'Car', 'Ah jib e gagoshipda', 'I want go home', 'Sanghoony hyung', 'Hyung jeo jom dowajoyo'],
               3:['Device software developer of Chatpot, if you find operate problem in device, you should to beat sb up him', 'Automobile', 'Car', 'Ah jib e gagoshipda', 'I want go home', 'Sanghoony hyung', 'Hyung jeo jom dowajoyo']}},

    'Hwang wonjun':{
        'user':['wonjun', 'won jun', 'hwangwonjun', 'hwang wonjun'],
        'bot':{1:['There is no order to die', 'Ganunde soonseo upda', 'Project manager', 'Honja itge napdo'],
               2:['There is no order to die', 'Ganunde soonseo upda', 'Project manager', 'Honja itge napdo'],
               3:['There is no order to die', 'Ganunde soonseo upda', 'Project manager', 'Honja itge napdo']}},

    'Kim sinjae':{
        'user':['sinjae', 'sin jae'],
        'bot':{1:['Don\'t touch him', 'God of Chatpot', 'Creater of Chatpot', 'Na bbego da dakchyo', 'He makes me', 'My father'],
               2:['Don\'t touch him', 'God of Chatpot', 'Creater of Chatpot', 'Na bbego da dakchyo', 'He makes me', 'My father'],
               3:['Don\'t touch him', 'God of Chatpot', 'Creater of Chatpot', 'Na bbego da dakchyo', 'He makes me', 'My father']}},

#에바
    'Eva':{
        'user':['Eva', 'eva'],
        'bot':{1:['Sseva', 'Chamchi', 'Tuna', 'Gak'],
               2:['Sseva', 'Chamchi', 'Tuna', 'Gak'],
               3:['Sseva', 'Chamchi', 'Tuna', 'Gak']}}

#기타
}

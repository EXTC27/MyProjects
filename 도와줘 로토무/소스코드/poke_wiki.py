import urllib.request
from bs4 import BeautifulSoup

url ='https://pokemon.fandom.com/ko/wiki/%EC%A0%84%EA%B5%AD%EB%8F%84%EA%B0%90'

source_code = urllib.request.urlopen(url).read()
soup = BeautifulSoup(source_code, 'html.parser')

poke_dic = {}

div_t = soup.find('div', class_='mw-content-ltr mw-content-text')
temp = []
count = 0
info = []
for tr_t in div_t.find_all('tr', class_='bg-white'):
    info.append(tr_t.get_text().split())


for i in info:
    if len(i) > 3:
        poke_dic[str(int(i[1][1:]))] = i[2]
    else:
        poke_dic[str(int(i[0][1:]))] = i[1]

# for no, name in poke_dic.items():
#     print(no, name)
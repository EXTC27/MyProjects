from selenium import webdriver
from urllib.request import urlopen
from bs4 import BeautifulSoup
import pymysql
driver = webdriver.Chrome('C:/Users/multicampus/Downloads/chromedriver_win32/chromedriver')
driver.implicitly_wait(6)
driver.get('http://www.jobkorea.co.kr/starter/calendar/202002?Sel_Date=202002&GI_Ing_Stat_Code_Text=&Area_Code_Text=&Jobtype_Code_Text=&Major_Code_Text=&Edu_Level_Code_Text=&Work_Type_Code_Text=&Co_Type_Code_Text=&Is_Save=&Is_Scrap=0&Is_Interest=0&GI_Ing_Stat_Code=&Is_Open=0&Is_EndOpen=0&Co_Name=')
html = driver.page_source
soup = BeautifulSoup(html, 'html.parser')
db=pymysql.connect("localhost","ssafy","ssafy","crawling",charset="utf8")
cursor = db.cursor()

#print(soup)
day=[]
title=[]
condition=[]
data_result=soup.find('table',{'class':'starCalTb'}).find('tbody').findAll('tr')
for tr_data in data_result:
    tr_result=tr_data.findAll('td')
    for td_data in tr_result:
        if tr_data.find('td',{'class':'disable'}):
            continue
        day_tmp=str(td_data.find('strong',{'class':'day'})).replace('<strong class="day">', '').replace('</strong>','')
        link_result=tr_data.findAll('span',{'class':'link'})
        for link_data in link_result:
            day.append(day_tmp)
            title.append(link_data.a.text.replace('시작','').replace('마감','').replace('예상','').replace('발표','').replace('인적성',''))
            condition.append(link_data.find('a').strong.text)



data=[]
for i in range(len(day)):
    data.append(("20.02."+day[i],title[i],condition[i]))

print(data)
query="""insert into jobkorea(day,title,cnd) values (%s,%s,%s)"""
cursor.executemany(query, tuple(data))
db.commit()
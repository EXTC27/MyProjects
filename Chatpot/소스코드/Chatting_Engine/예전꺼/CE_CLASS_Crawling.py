# -*- coding: utf-8 -*-
from bs4 import BeautifulSoup
import requests

class Crawling:
    def weather(self):
        url = 'https://search.naver.com/search.naver'
        hrd = {'User-Agent':'Mozilla/5.0', 'referer':'http://naver.com'}
        database=['강남구 역삼1동']
        search_word = '강남구 역삼1동 날씨'

        params = {'query': search_word,
                  'where': 'nexearch'}

        try:
            weather = ''
            response = requests.get(url, params=params, headers=hrd)
            html = response.text
            soup = BeautifulSoup(html, 'html.parser')

            area_temp = soup.find('div', {'class': 'info_data'})
            temp = area_temp.find_all('span', {'class':'todaytemp'})
            comment = area_temp.find_all('p', {'class':'cast_txt'})

            area_mmtemp = soup.find('span', {'class':'merge'})
            min_max_temp = area_mmtemp.find_all('span', {'class':'num'})
            min_max = ['','']

            area_sens = soup.find('span', {'class':'sensible'})
            sensible = area_sens.find_all('span', {'class':'num'})

            area_uv = soup.find('span', {'class':'indicator'})
            ultraviolet = area_uv.find_all('span',{'class':'lv4'})

            area_dust = soup.find('div',{'class':'sub_info'})
            microdust = area_dust.find_all('dd',{'class':'lv1'})
            dust = ['','']

            for tag in comment:
                whea = self.find_keys('weather', tag.text)
                weather += 'Weather is ' + whea

            for tag in temp:
                weather += '\nTemperature of outside is ' + tag.text + '\'c'

            count = 0
            for tag in min_max_temp:
                min_max[count] = tag.text
                count += 1

                if count is 1:
                    weather += '\nMin temperature is ' + min_max[0] + '\'c'
                elif count is 2:
                    weather += ' and Max is ' + min_max[1] + '\'c'
                    break

            for tag in sensible:
                weather += '\nYou might feel like ' + tag.text +'\'c'

            for tag in ultraviolet:
                weather += '\nUltraviolet is ' + self.find_keys('dust_uv', tag.text)

            count = 0
            for tag in microdust:
                dust[count] = self.find_keys('dust_uv', tag.text)
                count += 1
                if count is 1:
                    weather += '\nMicrodust is ' + dust[0]
                elif count is 2:
                    weather += ' and Ultramicrodust is ' + dust[1]

                    break

        except:
            weather = 'Error, check your internet'
            pass

        return weather

    def find_keys(self, mode, tag):
        if mode is 'weather':
            if '맑음' in tag:
                return '\'Sunny\''
            elif '구름조금' in tag:
                return '\'Slightly covered sky\''
            elif '구름많음' in tag:
                return '\'Very cloudy sky\''

        elif mode is 'dust_uv':
            if '매우나쁨' in tag:
                return '\'Very bad\''
            elif '나쁨' in tag:
                return '\'Bad\''
            elif '보통' in tag:
                return '\'Normal\''
            elif '좋음' in tag:
                return '\'Good\''
            elif '매우좋음' in tag:
                return '\'Very good\''

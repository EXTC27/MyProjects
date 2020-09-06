from CE_DATA_PlantStandardDB import plant_DB

class Feeling:
    #info = plant_info
    #db = plant_DB[info['kind']]

    def __init__(self, plant_info):
        #print('{0}  {1}  {2}'.format(self.on_temp(), self.on_Grou(), self.on_ligh()))
        self.info = plant_info
        self.db = plant_DB[self.info['Species']]
        self.feeling = self.classify_rank(self.on_temp()[0], self.on_grou()[0], self.on_ligh()[0])
        self.detail = {'Temp':self.on_temp()[1], 'Grou':self.on_grou()[1], 'Ligh':self.on_ligh()[1]}


    def on_temp(self):
        if (float(self.info['Temp']) >= (float(self.db['Temperature'][0]) - 5)) & (float(self.info['Temp']) <= (float(self.db['Temperature'][1]) + 5)):
            rank_temp = [3, 'good']
            if (float(self.info['Temp']) >= self.db['Temperature'][0]) & (float(self.info['Temp']) <= self.db['Temperature'][1]):
                rank_temp = [4, 'great']
        elif float(self.info['Temp']) < (self.db['Temperature'][0] - 5):
            rank_temp = [2, 'cold']
            if float(self.info['Temp']) < (self.db['Temperature'][0] - 10):
                rank_temp = [1, 'very cold']
                if float(self.info['Temp']) < 0:
                    rank_temp = [0, 'freeze']
        elif float(self.info['Temp']) > (self.db['Temperature'][0] + 5):
            rank_temp = [2, 'hot']
            if float(self.info['Temp']) > (self.db['Temperature'][0] + 10):
                rank_temp = [1, 'very hot']
                if float(self.info['Temp']) > 40:
                    rank_temp = [0, 'burn']

        return rank_temp

    #def on_humi(self):


    def on_grou(self):
        if (float(self.info['Grou']) >= self.db['Ground'][0]) & (float(self.info['Grou']) <= self.db['Ground'][1]):
            rank_grou = [2, 'good']
        elif (float(self.info['Grou']) < self.db['Ground'][0]):
            rank_grou = [1, 'little']
            if (float(self.info['Grou']) < self.db['Ground'][0] - 20):
                rank_grou = [0, 'scant']
        elif (float(self.info['Grou']) > self.db['Ground'][1]):
            rank_grou = [1, 'much']
            if (float(self.info['Grou']) > self.db['Ground'][1] + 20):
                rank_grou = [0, 'flood']

        return rank_grou

    def on_ligh(self):
        if (float(self.info['Ligh']) >= self.db['Light'][0]) & (float(self.info['Ligh']) <= self.db['Light'][1]):
            rank_ligh = [1, 'good']
        elif (float(self.info['Ligh']) < self.db['Light'][0]):
            rank_ligh = [0, 'dark']
        elif (float(self.info['Ligh']) > self.db['Light'][1]):
            rank_ligh = [0, 'blinding']

        return rank_ligh

    def classify_rank(self, rank_temp, rank_grou, rank_ligh):
        rank_total = rank_temp + rank_grou + rank_ligh
        plant_feeling = 0
        if rank_total > 2:
            plant_feeling = 1
            if rank_total >4:
                plant_feeling = 2
                if rank_total > 5:
                    plant_feeling = 3
                    if rank_total > 6:
                        plant_feeling = 4

        return plant_feeling

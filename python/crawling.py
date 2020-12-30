from selenium import webdriver
import json
from time import sleep

class Crawler:
    def __init__(self, _user_id, _user_pw):
        self.driver = webdriver.Chrome("./chromedriver_win32/chromedriver.exe")
        self.user_id = _user_id
        self.user_pw = _user_pw
        self.driver.get("https://e-campus.khu.ac.kr/login")

    def login(self, user_id, user_pw):
        self.driver.find_element_by_xpath('//*[@id="login_user_id"]').send_keys(user_id)
        self.driver.find_element_by_xpath('//*[@id="login_user_password"]').send_keys(user_pw)
        self.driver.find_element_by_xpath('//*[@id="form1"]/div[4]/a').click()

    def select_calander(self):
        self.driver.get('https://khcanvas.khu.ac.kr/calendar')
        #driver.implicitly_wait(10)

    def atom_calander(self):
        atom = self.driver.find_element_by_xpath('/html/head/link[4]').get_attribute('href')
        return atom

    def __del__(self):
        self.driver.quit()

    def run(self):
        self.login(self.user_id, self.user_pw)
        self.select_calander()
        return(self.atom_calander())
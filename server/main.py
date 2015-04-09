# tornado
import tornado.ioloop
import tornado.web
# mongodb
import pymongo
# json
import json
import re
import datetime

conncection = pymongo.Connection('localhost')
db=conncection['pharmapp']

class MainHandler(tornado.web.RequestHandler):
    def get(self):
        self.write("Hello, world")

class Statistic(tornado.web.RequestHandler):
    def add_month(self,source):
        year = source.year + int(source.month/12)
        month = source.month %12 +1
        return datetime.datetime(year,month,1)
    def getrestultatdate(self,date):
        results = []
        pipeline =[{"$match": {"$date":{"$gte": date,"$lt": self.add_month(date)}}},{"$group":{ "_id": "$name","count": {"$sum": "$count"},"price": {"$sum": {"$multiply": ["$price","$count"]}}}}] 
        for result in db.past.aggregate(pipeline)["result"]:
            result['name']=result['_id']
            results.append(result)
        return results
    def getrestult(self):
        results = []
        pipeline =[{"$group":{ "_id": "$name","count": {"$sum": "$count"},"price": {"$sum": {"$multiply": ["$price","$count"]}}}}] 
        for result in db.past.aggregate(pipeline)["result"]:
            result['name']=result['_id']
            results.append(result)
        return results
    def get(self,date):
        if re.match("[01][0-9]-[12][0-9]{3}",date):
            parsedDate=datetime.datetime(int(date[4:7]),int(date[0:2]),1)
            parsed2=self.add_month(parsedDate)
            
            self.write(json.dumps(self.getrestult(parsedDate=datetime.datetime(int(date[4:7]),int(date[0:2]),1))))
        else:
            self.write(json.dumps(self.getrestult()))
    
class SearchDrugs(tornado.web.RequestHandler):
    def getrestult(self,name):
        results = []
        for result in db.drugs.find({ "name": {"$regex": ".*"+name+".*" }} ):
            result['_id']=str(result['_id'])
            results.append(result)
        return results
    def get(self,name):
        self.write(json.dumps(self.getrestult(name)))

application = tornado.web.Application([
    (r"/", MainHandler),
    (r"/search/([a-zA-Z ]*)", SearchDrugs),
    (r"/statisticFrom/([- 0-9a-zA-Z]*)", Statistic),
])

if __name__ == "__main__":
    application.listen(8888)
    tornado.ioloop.IOLoop.instance().start()

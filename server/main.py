# tornado
import tornado.ioloop
import tornado.web
# mongodb
import pymongo
# json
import json

conncection = pymongo.Connection('localhost')
db=conncection['pharmapp']

class MainHandler(tornado.web.RequestHandler):
    def get(self):
        self.write("Hello, world")

class Statistic(tornado.web.RequestHandler):
    def getrestult(self,a):
        results = []
        pipeline =[{"$group":{ "_id": "$name","cena": {"$sum": {"$multiply": ["$price","$count"]}}}}] 
        for result in db.past.aggregate(pipeline)["result"]:
            results.append(result)
        return results
    def get(self,name):
        self.write(json.dumps(self.getrestult(name)))
    
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
    (r"/statisticFrom/([a-zA-Z ]*)", Statistic),
])

if __name__ == "__main__":
    application.listen(8888)
    tornado.ioloop.IOLoop.instance().start()

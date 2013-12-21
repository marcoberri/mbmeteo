var fieldsFix = {
        "outdoorHumidity":"indoorHumidity",
        "outdoorTemperature":"indoorTemperature"
};


for(var key in fieldsFix ) {
    var value = fieldsFix[key];
    print("key:" + key);
    print("value:" + value);
    var find = "{"+key+":{$exists:false}," + value + ":{$exists:true}}";
    var result = db.Meteolog.find(find);
    
    result.forEach(function(item) {
        print("Update: " + item["_id"] + " set: " + key + " to: " + value);
        db.Meteolog.update({"_id":item["_id"]},{$set : {"fixed":true, key : item[value]} });
    });
    
}

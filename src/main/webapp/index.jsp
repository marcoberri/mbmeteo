<%@page contentType="text/html" pageEncoding="UTF-8" import="it.marcoberri.mbmeteo.model.MapReduceHistoryMinMax, it.marcoberri.mbmeteo.utils.StringUtil, java.util.ArrayList, java.util.Date, java.util.Map, it.marcoberri.mbmeteo.utils.Default, it.marcoberri.mbmeteo.utils.DateTimeUtil, it.marcoberri.mbmeteo.helper.ConfigurationHelper" %>
<%@page import="java.util.TreeMap"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="it.marcoberri.mbmeteo.action.Commons"%>
<%
    String from = "";
    String to = "";
    if (application.getAttribute("from") != null) {
        from = DateTimeUtil.dateFormat("yyyy-MM-dd", (Date) application.getAttribute("from"));
    }

    MapReduceHistoryMinMax datesHistory = null;
    if (application.getAttribute("history") != null) {
        datesHistory = (MapReduceHistoryMinMax) application.getAttribute("history");
    }

    if (application.getAttribute("to") != null) {
        to = DateTimeUtil.dateFormat("yyyy-MM-dd", (Date) application.getAttribute("to"));
    }

    if (request.getParameterMap().get("from") != null) {
        String[] v = (String[]) request.getParameterMap().get("from");
        from = Default.toString(v[0]);
    }
    if (request.getParameterMap().get("to") != null) {
        String[] v = (String[]) request.getParameterMap().get("to");
        to = Default.toString(v[0]);
    }


    final Date lastUpdate = (Date) ((application.getAttribute("lastUpdate") != null) ? application.getAttribute("lastUpdate") : new Date());
    final Date activeFrom = (Date) ((application.getAttribute("activeFrom") != null) ? application.getAttribute("activeFrom") : new Date());


    final String bigDimx = "850";
    final String bigDimy = "450";


    final String[] fields = {"outdoorTemperature", "outdoorHumidity", "absolutePressure", "wind", "windChill", "gust", "dewpoint"};

    ArrayList<String> periods = new ArrayList<String>();
    periods.add("day");


%>
<!DOCTYPE html>
<html lang="it">
    <head>
        <meta charset="utf-8">
        <title><%=ConfigurationHelper.prop.getProperty("app.title")%></title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="<%=ConfigurationHelper.prop.getProperty("app.decription")%>">
        <meta name="author" content="Marco Berri">
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/jquery.lightbox-0.5.css" rel="stylesheet"%>
        <link href="css/jquery-ui-1.9.2.custom.min.css" rel="stylesheet">
        <style type="text/css">
            body {
                padding-top: 60px;
                padding-bottom: 40px;
            }
        </style>

        <link href="css/bootstrap-responsive.min.css" rel="stylesheet">
        <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
        <!--[if lt IE 9]>
          <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->
        <link rel="apple-touch-icon-precomposed" sizes="144x144" href="ico/apple-touch-icon-144-precomposed.png">
        <link rel="apple-touch-icon-precomposed" sizes="114x114" href="ico/apple-touch-icon-114-precomposed.png">
        <link rel="apple-touch-icon-precomposed" sizes="72x72" href="ico/apple-touch-icon-72-precomposed.png">
        <link rel="apple-touch-icon-precomposed" href="ico/apple-touch-icon-57-precomposed.png">
    </head>

    <body itemscope itemtype="http://schema.org/CollectionPage">

        <div id="divlog"></div>


        <div class="navbar navbar-inverse navbar-fixed-top">
            <div class="navbar-inner">
                <div class="container">
                    <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </a>
                    <a class="brand" href="#"><%=ConfigurationHelper.prop.getProperty("app.brand")%></a>

                    <div class="nav-collapse collapse">
                        <ul class="nav">
                            <li><a href="javascript:void();" onClick="$('#divlog').load('logdata.jsp').dialog({modal: true, closeText: 'Close', minHeight: 400, minWidth: 850, title: 'Meteo Log Archive'})">All Data</a></li>
                            <li><a data-toggle="modal" href="#history">History</a></li>
                            <li><a data-toggle="modal"  href="#info">Info</a></li>

                        </ul>
                    </div><!--/.nav-collapse -->

                </div>
            </div>
        </div>

        <div class="container">

            <div class="hero-unit" style="padding:5px;">

                <div class="row">      

                    <div class="span2">
                        <img src="img/front.jpg"/>
                    </div>

                    <div class="span4">
                        <p>
                        <address>
                            GPS Position: 45° 22.080'N - 8° 10.593'E<br/> 
                            Station: PCE FWS-20<br/> 
                            Location: Via Palestro, 8 Santhi&agrave; (VC) 13048 Italy<br/>  
                        </address>
                        </p>


                        <p id="activefrom">Active From: <%=DateTimeUtil.dateFormat("dd-MM-yyyy", activeFrom)%></p>
                        <p></p>
                        <p id="lastupdateapp">Last Update: <%=DateTimeUtil.dateFormat("dd-MM-yyyy HH:mm", lastUpdate)%></p>


                    </div>

                    <div class="span4">
                     <div class="col-md-4">10/10/2010. Add support</div>
                     <div class="col-md-4">10/10/2010. Add support</div>
                     <div class="col-md-4">10/10/2010. Add support</div>
                    </div>

                        
                </div>
            </div>

            <div class="row" style="margin-top:5px;">      

                <div class="span12">

                    <ul class="nav nav-pills" id="myTab">
                        <%-- <li class="active"><a href="#now">Now</a></li>  --%>
                        <li class="active"><a href="#outdoorTemperature">Temperature</a></li>
                        <li><a href="#outdoorHumidity">Humidity</a></li>
                        <li><a href="#absolutePressure">Absolute Pressure</a></li>
                        <li><a href="#wind">Wind</a></li>
                        <li><a href="#windChill">Wind Chill</a></li>
                        <li><a href="#windLevel">Wind Level</a></li>
                        <li><a href="#gust">Wind Gust</a></li>
                        <li><a href="#dewpoint">Dewpoint</a></li>
                        <li><a href="#rainfall">Rain Fall</a></li>
                        <li><a href="#historychart">History Compare</a></li>
                    </ul>



                    <%
                        final TreeMap<String, Date> dates = Commons.getDistinctTime();
                        final DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    %>

                   <!-- <form class="form-inline" action="" method="post">
                        <fieldset>
                            <label class="control-label" for="comboFrom">From</label> 
                            <select name="from" class="span2" id="comboFrom">
                                <% for (String key : dates.keySet()) {%>
                                <option name="<%=key%>" value="<%=key%>" <%=(key.equals(from) ? "selected=\"selected\"" : "")%>><%=df.format(dates.get(key))%></option>
                                <%}%> 
                            </select>
                            <label class="control-label" for="comboTo">To</label> 
                            <select  name="to" class="span2" id="comboTo">
                                <% for (String key : dates.keySet()) {%>
                                <option name="<%=key%>" value="<%=key%>" <%=(key.equals(to) ? "selected=\"selected\"" : "")%>><%=df.format(dates.get(key))%></option>
                                <%}%> 
                            </select>
                            <button type="submit" class="btn">Filter</button>
                        </fieldset>
                    </form>
                   -->

                    <div class="tab-content"  style="text-align:center">

                        <%--
                      <div class="tab-pane active" id="now">

                            <div class="row" class="span12" > 
                                <div class="span6" >
                                    <img src="GetLastTBar?dimx=300&dimy=300"/>
                                </div>                        
                                <div class="span6" >
                                    <img src="GetLastUPie?dimx=300&dimy=300"/>
                                </div>                        
 
                            </div>
                        </div>
                        --%>
                        <%
                            boolean active = true;
                            for (String f : fields) {
                        %>                       

                        <div class="tab-pane<%if (active) {
                                out.println(" active");
                                active = false;
                            }%>"  id="<%=f%>">

                            <div id="gallery">

                                <div class="row" class="span12" >  
                                    <div class="span12" >
                                        <a alt="<%=f%>" href="getChart?field=<%=f%>&from=<%=from%>&to=<%=to%>&dimx=<%=bigDimx%>&dimy=<%=bigDimy%>"><img src="getChart?field=<%=f%>&from=<%=from%>&to=<%=to%>&dimx=<%=bigDimx%>&dimy=<%=bigDimy%>" style="height:<%=bigDimy%>; width:<%=bigDimx%>" alt="<%=f%>"/></a>
                                    </div>
                                </div>
                            </div>

                            <% for (String p : periods) {%>
                            <div class="row" class="span12" style="text-align:center"> 
                                <div class="span12">
                                    <img src="getChartMinAndMax?field=<%=f%>&from=<%=from%>&to=<%=to%>&period=<%=p%>&type=minmax&dimx=<%=bigDimx%>&dimy=<%=bigDimy%>" style="height:<%=bigDimy%>; width:<%=bigDimx%>"/>
                                </div>                              

                            </div>
                            <%
                                }
                            %>
                        </div>
                        <%
                            }
                        %>






                        <div class="tab-pane" id="windLevel">
                            <img src="getChart?field=windLevel&from=<%=from%>&to=<%=to%>"/>
                        </div>  

                        <div class="tab-pane" id="gust">
                            <img src="getChart?field=gust&from=<%=from%>&to=<%=to%>"/>
                        </div>     

                        <div class="tab-pane" id="rainfall">
                            <div>
                                <img src="getChart?field=hourRainfall&from=<%=from%>&to=<%=to%>&dimy=300"/>
                            </div>     

                            <div>
                                <img src="getChart?field=dayRainfall&from=<%=from%>&to=<%=to%>&dimy=300"/>
                            </div>     

                            <div>
                                <img src="getChart?field=weekRainfall&from=<%=from%>&to=<%=to%>&dimy=300"/>
                            </div>   

                            <div>
                                <img src="getChart?field=monthRainfall&from=<%=from%>&to=<%=to%>&dimy=300"/>
                            </div>   


                            <div>
                                <img src="getChart?field=totalRainfall&from=<%=from%>&to=<%=to%>&dimy=300"/>
                            </div>   

                        </div>

                        <div class="tab-pane" id="historychart">
                            <%
                                for (int i = 1; i < Default.toInt(DateTimeUtil.getNowWithFormat("MM"), 1); i++) {
                            %>                            
                            <div>
                                <a alt="Outdoor Temperature - from 01-<%=i%>-2013 to from 31-<%=i%>-2013" href="getChart?field=outdoorTemperature&from=2013-<%=i%>-01&to=2013-<%=i%>-31&dimx=<%=bigDimx%>&dimy=<%=bigDimy%>"><img src="getChart?field=outdoorTemperature&from=2013-<%=i%>-01&to=2013-<%=i%>-31&dimy=300"/></a>
                            </div>
                            <%
                                }
                            %>                            
                        </div>  

                    </div>

                </div>
            </div>

            <footer>
                <p>&copy; <a href="http://marcoberri.blogspot.com">Marco e Cinzia Blog</a> - webapp from <a href="http://tecnicume.blogspot.com">Marco</a> - <a href="http://code.google.com/p/mbmeteo">Here</a></p>
            </footer>

        </div> <!-- /container -->



        <%
            if (datesHistory != null) {
        %>
        <div id="history" class="modal hide fade in" style="display: none; ">  
            <div class="modal-header">  
                <a class="close" data-dismiss="modal">×</a>  
                <h3>History</h3>  
            </div>  
            <div class="modal-body">  
                <h4>History Data</h4>  
                <p>

                <ul>
                    <li>Temperature Max:  <%=datesHistory.getOTMax()%> °C at <%=datesHistory.getOTMaxTime()%></li>
                    <li>Temperature Min:  <%=datesHistory.getOTMin()%> °C at <%=datesHistory.getOTMinTime()%></li>
                    <li>Humidity Max:  <%=datesHistory.getOHMax()%> % at <%=datesHistory.getOHMaxTime()%></li>
                    <li>Humidity Min:  <%=datesHistory.getOHMin()%> % at <%=datesHistory.getOHMinTime()%></li>
                    <li>Relative Pressure Max:  <%=datesHistory.getRPMax()%> Hpa at <%=datesHistory.getRPMaxTime()%></li>
                    <li>Relative Pressure Min:  <%=datesHistory.getRPMin()%> Hpa at <%=datesHistory.getRPMinTime()%></li>
                    <li>Absolute Pressure Max:  <%=datesHistory.getAPMax()%> Hpa at <%=datesHistory.getAPMaxTime()%></li>
                    <li>Absolute Pressure Min:  <%=datesHistory.getAPMin()%> Hpa at <%=datesHistory.getAPMinTime()%></li>
                    <li>Wind Max:  <%=datesHistory.getWindMax()%> m/s at <%=datesHistory.getWindMaxTime()%></li>
                    <li>Dewpoint Max:  <%=datesHistory.getDewpointMax()%> °C at <%=datesHistory.getDewpointMaxTime()%></li>
                    <li>Dewpoint Min  <%=datesHistory.getDewpointMin()%> °C at <%=datesHistory.getDewpointMinTime()%></li>
                </ul>
                </p>                

            </div>  
            <div class="modal-footer">  
                <a href="#" class="btn" data-dismiss="modal">Close</a>  
            </div>  
        </div>  

        <%
            }
        %>


        <div id="info" class="modal hide fade in" style="display: none; ">  
            <div class="modal-header">  
                <a class="close" data-dismiss="modal">×</a>  
                <h3>Info</h3>  
            </div>  
            <div class="modal-body">  
                <h4>Info Data</h4>  
                <p>

                <ul>
                    <li>Web Server : Jetty Server</li>
                    <li>Ide : <a href="http://www.netbeans.org">NetBeans IDE 7.2.1 on Ubuntu 12.10</a></li>
                    <li>DB : <a href="http://www.mongodb.org">MongoDB</a></li>
                    <li>OS : Ubuntu Server 12.10</li>
                    <li>Server : <a href="http://www.linode.com">Linode</a></li>
                    <li>Source : <a href="http://code.google.com/p/mbmeteo">Google code</a></li>
                    <li>Tech Blog : <a href="http://tecnicume.blogspot.it">Tecnicume</a></li>
                    <li>Total Record : <%=application.getAttribute("totRecord")%> </li>
                </ul>
                </p>                
            </div>  
            <div class="modal-footer">  
                <a href="#" class="btn" data-dismiss="modal">Close</a>  
            </div>  
        </div> 



        <!-- javascript ================================================== -->

        <script src="js/jquery-1.8.3.min.js"></script>
        <script src="js/jquery-ui-1.9.2.custom.min.js"></script>
        <script src="js/jquery.lightbox-0.5.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script src="js/main.js"></script>


        <script type="text/javascript">
                                var _gaq = _gaq || [];
                                _gaq.push(['_setAccount', '<%=ConfigurationHelper.prop.getProperty("analytics.code")%>']);
                                _gaq.push(['_trackPageview']);

                                (function() {
                                    var ga = document.createElement('script');
                                    ga.type = 'text/javascript';
                                    ga.async = true;
                                    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
                                    var s = document.getElementsByTagName('script')[0];
                                    s.parentNode.insertBefore(ga, s);
                                })();
        </script>

    </body>
</html>

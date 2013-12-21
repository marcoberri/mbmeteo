Ext.require([
    'Ext.grid.*',
    'Ext.data.*',
    'Ext.util.*',
    'Ext.state.*',
    'Ext.chart.*',
    'Ext.layout.container.Fit', 
    'Ext.fx.target.Sprite', 
    'Ext.toolbar.Paging'
    ]);

//modello


function change(val) {
    if (val > 0) {
        return '<span style="color:green;">' + val + '</span>';
    } else if (val < 0) {
        return '<span style="color:red;">' + val + '</span>';
    }
    return val;
}

function changeData(val) {
    return val.substring(6,8) + "-"+ val.substring(4,6) + "-"+ val.substring(0,4) + " " + val.substring(8,10) + ":" + val.substring(10,12);
}


/**
     * Custom function used for column renderer
     * @param {Object} val
     */
function pctChange(val) {
    if (val > 0) {
        return '<span style="color:green;">' + val + '%</span>';
    } else if (val < 0) {
        return '<span style="color:red;">' + val + '%</span>';
    }
    return val;
}
    


    
Ext.define('Meteolog', {
    extend: 'Ext.data.Model',
    fields: [
    {
        name: 'id',  
        type: 'float', 
        convert: null, 
        defaultValue: undefined
    },

    {
        name: 'n',  
        type: 'float', 
        convert: null, 
        defaultValue: undefined
    },

    {
        name: 'time', 
        dateFormat:'YmdHi', 
        /*format: 'd/m/Y H:i',submitFormat: 'YmdHi',*/type: 'date'/* ,convert: null*/
    },

    {
        name: 'outdoorHumidity',  
        type: 'float', 
        convert: null, 
        defaultValue: undefined
    },

    {
        name: 'outdoorTemperature',  
        type: 'float', 
        convert: null, 
        defaultValue: undefined
    },

    {
        name: 'absolutePressure',  
        type: 'float', 
        convert: null, 
        defaultValue: undefined
    },

    {
        name: 'relativePressure',  
        type: 'float', 
        convert: null, 
        defaultValue: undefined
    },      

    {
        name: 'wind',  
        type: 'float', 
        convert: null, 
        defaultValue: undefined
    },

    {
        name: 'windChill',  
        type: 'float', 
        convert: null, 
        defaultValue: undefined
    },

    {
        name: 'windLevel',  
        type: 'float', 
        convert: null, 
        defaultValue: undefined
    },

    {
        name: 'gust',  
        type: 'float', 
        convert: null, 
        defaultValue: undefined
    },

    {
        name: 'gustLevel',  
        type: 'float', 
        convert: null, 
        defaultValue: undefined
    },

    {
        name: 'direction'
    },

    {
        name: 'dewpoint',  
        type: 'float', 
        convert: null, 
        defaultValue: undefined
    },

    {
        name: 'hourRainfall',  
        type: 'float', 
        convert: null, 
        defaultValue: undefined
    },

    {
        name: 'dayRainfall',  
        type: 'float', 
        convert: null, 
        defaultValue: undefined
    },

    {
        name: 'weekRainfall',  
        type: 'float', 
        convert: null, 
        defaultValue: undefined
    },

    {
        name: 'monthRainfall',  
        type: 'float', 
        convert: null, 
        defaultValue: undefined
    },    

    {
        name: 'totalRainfall',  
        type: 'float', 
        convert: null, 
        defaultValue: undefined
    }
    ],
    idProperty: 'n'
});


   
var allLogStore = Ext.create('Ext.data.Store', {
    storeId: 'allLogStore',
    model: 'Meteolog',
    autoLoad: 'true',
    proxy: {
        type: 'ajax',
        url: 'get',
        reader: {
            root: 'data',
            totalProperty: 'totalCount'
        }

    }
        
});




function chartView(chartName, chartTitle, chartIdtarget, chartField){

    Ext.create('Ext.chart.Chart', {
        store: globalStore,
        width: 600,
        height: 300,
        renderTo: chartIdtarget,

        axes: [
        {

            type: 'Numeric',
            position: 'left',
            fields: [chartField]

        }, {
            title: 'Time',
            type: 'Time',
            dateFormat : 'M d h',
            position: 'bottom',
            fields: ['time']

        }],
        series: [{
            type: 'line',
            axis: 'left',
            xField: 'time',
            yField: chartField
        }]

    });

}



function grid(st){
    
    
    // create the Grid
   Ext.create('Ext.grid.Panel', {
        store: st,
        collapsible: false,
        multiSelect: false,
        stateId: 'stateGrid',
   
        height: 450,
        width: 800,
        title: 'Add Data',
        renderTo: 'grid',
        viewConfig: {
            trackOver: false,
            stripeRows: false

        },
        
        
        bbar: Ext.create('Ext.PagingToolbar', {
            store: st,
            displayInfo: true,
            displayMsg: '{0} - {1} of {2}',
            emptyMsg: "No log to display"
            
        }),  
   
        columns: [
        {
            text     : 'Time',
            flex: 1,
            sortable : true,

            dataIndex: 'time'
        },           
        {
            text     : 'Humidity %',
            flex: 1,
            sortable : true,
            renderer : pctChange,
            dataIndex: 'outdoorHumidity'
        },
        {
            text     : 'T &#176;C',
            flex: 1,
            sortable : true,
            renderer : change,
            dataIndex: 'outdoorTemperature'
        },

        {
            text     : 'Absolute Pressure(Hpa)',
            flex: 1,
            sortable : true,
            renderer : change,
            dataIndex: 'absolutePressure'
        }, 

        {
            text     : 'Relative Pressure (Hpa)',
            flex: 1,
            sortable : true,
            renderer : change,
            dataIndex: 'relativePressure'
        }, 
        {
            text     : 'Wind (m/s)',
            flex: 1,
            sortable : true,
            renderer : change,
            dataIndex: 'wind'
        },           
        {
            text     : 'Gust',
            flex: 1,
            sortable : true,
            renderer : change,
            dataIndex: 'gust'
        },            
        {
            text     : 'Wind Direction',
            flex: 1,
            sortable : true,
            dataIndex: 'direction'
        },
                 
        {
            text     : 'Rain mm/h',
            flex: 1,
            sortable : true,
            dataIndex: 'hourRainfall'
        },

                 
        {
            text     : 'Rain mm/24h',
            flex: 1,
            sortable : true,
            dataIndex: 'dayRainfall'
        },

                 
        {
            text     : 'Rain mm/7days',
            flex: 1,
            sortable : true,
            dataIndex: 'weekRainfall'
        },
        {
            text     : 'Rain mm/30days',
            flex: 1,
            sortable : true,
            dataIndex: 'monthRainfall'
        },
        {
            text     : 'Rain Tot. mm',
            flex: 1,
            sortable : true,
            dataIndex: 'totalRainfall'
        }

        ]
    
    });  
    
    
}

Ext.onReady(function() {
     grid(allLogStore);
     $("#wait").hide();
});



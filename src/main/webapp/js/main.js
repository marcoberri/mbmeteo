
function setCombo(name1, name2){
    $.getJSON("getDistinctTime",    
        function(json) {    
            $.each(json, function(key, value) {
                var toselect1 = $(name1).attr('mb:toselect');
                     
                if(key == toselect1)
                    $(name1).append($("<option></option>").attr("value",key).attr("selected","selected").text(value));
                else
                    $(name1).append($("<option></option>").attr("value",key).text(value)); 
    
                var toselect2 = $(name2).attr('mb:toselect');
                     
                if(key == toselect2)
                    $(name2).append($("<option></option>").attr("value",key).attr("selected","selected").text(value));
                else
                    $(name2).append($("<option></option>").attr("value",key).text(value)); 
    
            }
            )
        }
        );
}

$(document).ready(function() {

  $('#myTab a').click(function (e) {
        e.preventDefault();
        $(this).tab('show');
    })

  //setCombo('#comboFrom','#comboTo');
  
  $('#gallery a').lightBox();
  
});

  




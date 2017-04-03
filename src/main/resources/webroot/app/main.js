$(document).ready(function () {
    
    var source = $("#movies").html();
    var template = Handlebars.compile(source);
    
    onLoad();

    function onLoad() {
        $.ajax({
            url: "/movies/",
            method: "get",
            dataType: "json",
            success: function (data) {
                console.log(data);
                displayData(data);
            },
            error: function (err) {
                console.log(err);
            }
        });
    }

    function displayData(data) {
        $("#append-movies").empty();
        $('#append-movies').append(template(data));
    }

    $("#append-movies").on('click', '.delete-movie',function () {
        var id = $(this).closest("td").attr("id");
        console.log(id);

         $.ajax({
            url: "/movies/"+id,
            method: "delete",
            dataType: "json",
            success: function (data){
                console.log(data);
                onLoad();
            },
            error: function (err){
                console.log(err);
            }
         });
    });

    $("#add-movie").on('click', function () {
        var name = $("#movie-title").val();
        var link = $("#movie-link").val();
        var json = JSON.stringify({title: name, link: link});
        
        $.ajax({
           url: "/movies/",
           method: "post",
           data: json,
           dataType: "json",
           success: function (data) {
               console.log(data);
               onLoad();
           },
           error: function (err) {
               console.log(err);
           }
        });

        $('#new-movie-modal').modal('hide');
    });
    
    
     $("#search").on('keyup',function(){
        var self = this;
        
        $.each($("#append-movies tr"), function() {
            if($(this).text().toLowerCase().indexOf($(self).val().toLowerCase()) === -1)
               $(this).hide();
            else
               $(this).show();                
        });
    }); 
});
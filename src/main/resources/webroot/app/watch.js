$(document).ready(function () {
    
    var source = $("#movies").html();
    var template = Handlebars.compile(source);
    
    onLoad();

    function onLoad() {
        var uris = window.location.pathname.split("/");
        var id = uris[uris.length-1];
        console.log(id);
        $.ajax({
            url: "/movies/" + id,
            method: "get",
            dataType: "json",
            success: function (data) {
                console.log(data.rows[0]);
                data.rows[0].hash = data.rows[0].link.split("=")[1];
                displayData(data.rows[0]);
            },
            error: function (err) {
                console.log(err);
            }
        });
    }

    function displayData(data) {
        $(".watch-content").empty();
        $('.watch-content').append(template(data));
    }
    
});



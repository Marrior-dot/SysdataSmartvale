$(document).ready(function () {
    if ($("#action").val() == "visualizando") {
        $("input:not([type=image],[type=button],[type=submit],[type=hidden],[type=search],[class=enable])").each(disable);
        $('select').each(disable);
    }
});

function disable() {
    $(this).attr("disabled", "true");
}
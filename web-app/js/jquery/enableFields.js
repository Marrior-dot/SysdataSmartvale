$(document).ready(function () {
    if ($("#action").val() == "visualizando") {
        $("select:not([id=estabs])").each(disable);
        $("input:not([type=image],[type=button],[type=submit],[type=hidden],[type=search],[type=enable],[class=enable])").each(disable);
        $("textarea").each(disable);
    }
});

function disable() {
    $(this).attr("disabled", "true");
}
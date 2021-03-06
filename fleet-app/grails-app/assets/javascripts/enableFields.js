$(document).ready(function () {
    if ($("#action").val() == "visualizando") {
        $("select:not([id=estabs]):not('.enable')").each(disable);
        $("input:not([type=image],[type=button],[type=submit],[type=hidden],[type=search],[type=enable]):not('.enable')").each(disable);
        $("textarea").each(disable);
    } else if ($("#action").val() == "editando") {
        $("select:not('.enable'):not('.editable')").each(disable);
        $("input:not([type=image],[type=button],[type=submit],[type=hidden],[type=search],[type=enable]):not('.enable'):not('.editable')").each(disable);
        $("textarea").each(disable);
    }
});

function disable() {
    $(this).attr("disabled", "true");
}
function parseCurrency(str) {
    var s = str.replace(/[^0-9,]+/g, "").replace(/,/g, ".");
    return parseFloat(s);
}

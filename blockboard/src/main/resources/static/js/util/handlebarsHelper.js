Handlebars.registerHelper('isTemp', function(option) {
    if(this.temp == true) {
        return option.fn(this);
    } else {
        return option.inverse(this);
    }
});
Handlebars.registerHelper('isAbleFunction', function(options) {
    if (this.companyID ==0) {
        return options.fn(this);
    }
    return options.inverse(this);;
});
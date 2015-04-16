
/**
 * Module dependencies.
 */

var express = require('express')
  , routes = require('./routes')
  , user = require('./routes/user')
  , http = require('http')
  , path = require('path')
  , session = require('express-session');

var app = express();
app.use(express.cookieParser());   
app.use(express.session({resave: false,
	  saveUninitialized: true,secret:'adfasdf34etydfs34sefsdf'}));
app.use(function(req, res, next) {
    res.header('Cache-Control', 'no-cache, private, no-store, must-revalidate, max-stale=0, post-check=0, pre-check=0');
    next();
  });
// all environments
app.set('port', process.env.PORT || 3013);
app.set('views', __dirname + '/views');
app.set('view engine', 'ejs');
app.use(express.favicon());
app.use(express.logger('dev'));
app.use(express.bodyParser());
app.use(express.methodOverride());
app.use(app.router);
app.use(express.static(path.join(__dirname, 'public')));

// development only
if ('development' == app.get('env')) {
  app.use(express.errorHandler());
}

app.get('/', routes.index);
app.get('/reg', routes.reg);
app.get('/dashboard', routes.dashboard);
app.post('/profile/login', routes.login);
app.post('/profile/signup', routes.signup);
app.post('/vm/create', routes.createvm);
app.post('/vm/stop', routes.shutdownvm);
app.post('/vm/start', routes.startvm);
app.post('/vm/terminate', routes.terminatevm);
app.post('/vm/getstate', routes.getstate);
app.post('/vm/getip', routes.getip);
app.post('/vm/getstats', routes.getstats);

app.post('/vm/rdp/create', routes.createrdp);

http.createServer(app).listen(app.get('port'), function(){
  console.log('Express server listening on port ' + app.get('port'));
});



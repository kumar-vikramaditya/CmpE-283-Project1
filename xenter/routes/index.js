/*
 * GET home page.
 */
var fs = require('fs');

var replace = require("replace");
var request = require("request");
// use:

exports.index = function(req, res) {
	res.render('index', {
		title : 'Express'
	});
};
exports.terminatevm = function(req, res) {
	var userId =req.session.userId;
	if (userId == undefined) {
		res.send({
			status : "error"
		})
	} else {
		
		console.log(req.param("vmid"));
		console.log(userId);
		var vmid = req.param("vmid");
		
		var requestdata = {
				 vmName:"",
				 vmOS:"",
				 vmCPU:"",
				 vmRAM:"",
				 vmDisk:"",
				 template:vmid,
				 userId:userId,
				 vmStatus:""
			};
		
		request({
			uri : "http://localhost:8080/api/v1/" + userId + "/vm/"+vmid+"/terminate",
			method : "POST",
			json : true,
			headers : {
				"content-type" : "application/json",
			},
			body : JSON.stringify(requestdata)
		}, function(error, response, body) {
			var l = body;
			// console.log(JSON.stringify(body));
			res.send({
				status:body
			});
		});
	}
}


exports.shutdownvm = function(req, res) {
	var userId = req.session.userId;
	
	console.log(req.param("vmid"));
	console.log(userId);
	
	if (userId == undefined) {
		res.send({
			status : "error"
		})
	} else {
		
		console.log(req.param("vmid"));
		console.log(userId);
		var vmid = req.param("vmid");
		
		var requestdata = {
				 vmName:"",
				 vmOS:"",
				 vmCPU:"",
				 vmRAM:"",
				 vmDisk:"",
				 template:vmid,
				 userId:userId,
				 vmStatus:""
			};
		
		request({
			uri : "http://localhost:8080/api/v1/" + userId + "/vm/"+vmid+"/stop",
			method : "POST",
			json : true,
			headers : {
				"content-type" : "application/json",
			},
			body : JSON.stringify(requestdata)
		}, function(error, response, body) {
			var l = body;
			// console.log(JSON.stringify(body));
			res.send({
				status:body
			});
		});
	}
}


exports.startvm = function(req, res) {
	var userId = req.session.userId;
	
	console.log("VMID :- "+ req.param("vmid"));
	console.log(userId);
	
	if (userId == undefined) {
		res.send({
			status : "error"
		})
	} else {
		
		console.log(req.param("vmid"));
		console.log(userId);
		var vmid = req.param("vmid");
		
		var requestdata = {
				 vmName:"",
				 vmOS:"",
				 vmCPU:"",
				 vmRAM:"",
				 vmDisk:"",
				 template:vmid,
				 userId:userId,
				 vmStatus:""
			};
		
		request({
			uri : "http://localhost:8080/api/v1/" + userId + "/vm/"+vmid+"/vm/start",
			method : "POST",
			json : true,
			headers : {
				"content-type" : "application/json",
			},
			body : JSON.stringify(requestdata)
		}, function(error, response, body) {
			var l = body;
			// console.log(JSON.stringify(body));
			res.send({
				status:body
			});
		});
	}
}

exports.getip = function(req, res) {
	var userId = req.session.userId;
	
	console.log("VMID :- "+ req.param("vmid"));
	console.log(userId);
	
	if (userId == undefined) {
		res.send({
			status : "error"
		})
	} else {
		
		console.log(req.param("vmid"));
		console.log(userId);
		var vmid = req.param("vmid");
		
		var requestdata = {
				 vmName:"",
				 vmOS:"",
				 vmCPU:"",
				 vmRAM:"",
				 vmDisk:"",
				 template:vmid,
				 userId:userId,
				 vmStatus:""
			};
		
		request({
			uri : "http://localhost:8080/api/v1/" + userId + "/vm/"+vmid+"/vm/getip",
			method : "POST",
			json : true,
			headers : {
				"content-type" : "application/json",
			},
			body : JSON.stringify(requestdata)
		}, function(error, response, body) {
			var l = body;
			// console.log(JSON.stringify(body));
			res.send({
				status:body
			});
		});
	}
}



exports.getstats = function(req, res) {
	var userId = req.session.userId;
	var vmid = req.param("vmid");
	vmid = vmid.replace("ui-id-","");
	console.log("VMID :- "+ vmid );
	console.log("Coming through");
	console.log(userId);
	
	if (userId == undefined) {
		res.send({
			status : "error"
		})
	} else {
		
		console.log(vmid);
		console.log(userId);
		//var vmid = req.param("vmid");
		
		var requestdata = {
				 vmName:"",
				 vmOS:"",
				 vmCPU:"",
				 vmRAM:"",
				 vmDisk:"",
				 template:vmid,
				 userId:userId,
				 vmStatus:""
			};
		
		request({
			uri : "http://localhost:8080/api/v1/" + userId + "/vm/"+vmid+"/vm/getstats",
			method : "POST",
			json : true,
			headers : {
				"content-type" : "application/json",
			},
			body : JSON.stringify(requestdata)
		}, function(error, response, body) {
			console.log("Coming through");
			var l = body;
			console.log(JSON.stringify(body));
			res.send({
				data:body
			});
		});
	}
}



exports.getstate = function(req, res) {
	var userId = req.session.userId;
	
	console.log("VMID :- "+ req.param("vmid"));
	console.log(userId);
	
	if (userId == undefined) {
		res.send({
			status : "error"
		})
	} else {
		
		console.log(req.param("vmid"));
		console.log(userId);
		var vmid = req.param("vmid");
		
		var requestdata = {
				 vmName:"",
				 vmOS:"",
				 vmCPU:"",
				 vmRAM:"",
				 vmDisk:"",
				 template:vmid,
				 userId:userId,
				 vmStatus:""
			};
		
		request({
			uri : "http://localhost:8080/api/v1/" + userId + "/vm/"+vmid+"/vm/getState",
			method : "POST",
			json : true,
			headers : {
				"content-type" : "application/json",
			},
			body : JSON.stringify(requestdata)
		}, function(error, response, body) {
			var l = body;
			// console.log(JSON.stringify(body));
			res.send({
				status:body
			});
		});
	}
}

exports.createvm = function(req, res) {
	var userId = req.session.userId;
	if (userId == undefined) {
		res.send({
			status : "loggedout"
		})
	} else {
		console.log(req.param("templateid"));
		console.log(userId);
		var tid = req.param("templateid");
		
		var requestdata = {
				 vmName:"",
				 vmOS:"",
				 vmCPU:"",
				 vmRAM:"",
				 vmDisk:"",
				 template:tid,
				 userId:userId,
				 vmStatus:"",vmIdCounter:0
			};
		
		request({
			uri : "http://localhost:8080/api/v1/" + userId + "/vm/create",
			method : "POST",
			json : true,
			headers : {
				"content-type" : "application/json",
			},
			body : JSON.stringify(requestdata)
		}, function(error, response, body) {
			var l = body;
			// console.log(JSON.stringify(body));
			res.send({
				status:body
			});
		});
	}

};

exports.dashboard = function(req, res) {
	var userId = req.session.userId
	console.log("DashBoard");
	console.log(userId);
	request({
		uri : "http://localhost:8080/api/v1/" + userId + "/listAllVMs",
		method : "GET",
		headers : {
			"content-type" : "application/json",
		}
	}, function(error, response, body) {
		var l = body;
		console.log(JSON.stringify(body));
		res.render('dashboard', {
			vms : JSON.parse(body)
		});
	});

};
exports.reg = function(req, res) {
	res.render('reg', {
		title : 'Express'
	});
};
exports.panelcollapse = function(req, res) {
	res.render('panelcollapse', {
		title : 'Express'
	});
};
exports.submituser = function(req, res) {
	res.render('dashboard', {
		title : 'Express'
	});
};
exports.login = function(req, res) {
	//console.log(req);
	var p_email = req.body.email;
	var p_password = req.body.password;
	console.log(p_email);
	console.log(p_password);
	//res.setHeader('Content-Type', 'application/json');

	// Validate user details
	requestdata = {
			firstName : "",
			lastName : "",
			email : p_email,
			password : p_password,
			dob : "",
			phone : "",
			cardDetails : ""
		};
		request({
			uri : "http://localhost:8080/api/v1/signIn",
			method : "POST",
			json : true,
			headers : {
				"content-type" : "application/json",
			},
			body : JSON.stringify(requestdata)
		}, function(error, response, body) {
			//console.log("Body");
			//console.log(body);
			//console.log("Response");
			//console.log(response);
			
			var l = (body);
			// console.log(JSON.stringify(body));
			console.log(body);
			if (l.userId != null) {

				req.session.userId = l.userId;
				res.setHeader('Content-Type', 'application/json');
				res.send(JSON.stringify({
					status : "success"
				}));
			} else {
				res.send({
					status : "failure"
				})
			}
		});
};
exports.signup = function(req, res) {
	// console.log(req);

	var p_email = req.param("email");
	var p_password = req.param("password");
	var p_firstname = req.param("firstname");
	var p_lastname = req.param("lastname");
	var p_creditcard = req.param("creditcard");
	var p_phone = req.param("phone");
	var p_dob = req.param("dob");

	requestdata = {
		firstName : p_firstname,
		lastName : p_lastname,
		email : p_email,
		password : p_password,
		dob : p_dob,
		phone : p_phone,
		cardDetails : p_creditcard
	};
	request({
		uri : "http://localhost:8080/api/v1/signUp",
		method : "POST",
		json : true,
		headers : {
			"content-type" : "application/json",
		},
		body : JSON.stringify(requestdata)
	}, function(error, response, body) {
		var l = body;
		// console.log(JSON.stringify(body));
		console.log(body);
		if (l.userId != null) {

			req.session.userId = l.userId;
			res.setHeader('Content-Type', 'application/json');
			res.send(JSON.stringify({
				status : "success"
			}));
		} else {
			res.send({
				status : "failure"
			})
		}
	});

};
exports.createrdp = function(req, res) {
	var vmname = req.body.vmname;
	console.log(req.body.vmname);

	console.log(req.body.vmip);

	// console.log(req)
	// fs.createReadStream('main.rdp').pipe(fs.createWriteStream(vmname +
	// '.rdp'));
	replace({
		regex : "xxx.xxx.xxx.xxx",
		replacement : req.body.vmip,
		paths : [ vmname + '.rdp' ],
		recursive : true,
		silent : true
	});
	res.send(vmname);
};

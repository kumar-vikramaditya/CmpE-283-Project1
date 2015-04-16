

/*var vms = [
{ "id":"123" , "vmOS":"windows" , "vmRAM":"512" , "vmDisk":"500", "vmStatus":"running"} ];



$(document).ready(function () {
	for(var j=0; j < vms.length; j++){
	            $("#row"+vms[j].id).children("#2").html(vms[j].id);
	            $("#row"+vms[j].id).children("#3").html(vms[j].vmOS);
	            $("#row"+vms[j].id).children("#4").html(vms[j].vmRAM);
	            $("#row"+vms[j].id).children("#5").html(vms[j].vmDisk);
	            $("#row"+vms[j].id).children("#6").html(vms[j].vmStatus);
        }
});*/

/////Charrt 1

      google.load('visualization', '1.0', {'packages':['corechart']});

      google.setOnLoadCallback(drawChart);

      function drawChart() {
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Usage');
        data.addColumn('number', 'percentage');
        data.addRows([
            ['Used', 50],
            ['Unused',200]
        ]);

        // Set chart options
        var options = {'title':'Memory',
                       'width':250,
                       'height':200,
                        'is3D': true};

        var chart = new google.visualization.PieChart(document.getElementById('memory'));
        chart.draw(data, options);
       

      }
///////////////Chart 2

google.setOnLoadCallback(drawChart2);
    function drawChart2() {
        var data2 = new google.visualization.DataTable();
        data2.addColumn('string', 'Usage');
        data2.addColumn('number', 'percentage');
        data2.addRows([
            ['Used', 90],
            ['Unused',200]
        ]);

         var options2 = {'title':'Cpu',
                       'width':250,
                       'height':200,
                        'is3D': true,
                       'slices': {
            0: { color: 'purple' },
            1: { color: 'green' }
          }
                        };

        var chart2 = new google.visualization.PieChart(document.getElementById('cpu'));
        chart2.draw(data2, options2);
    }

/////////// chart 3
//google.load('visualization', '1', {packages: ['corechart']});
google.setOnLoadCallback(drawBasic);

function drawBasic() {

    var data = new google.visualization.DataTable();
    data.addColumn('date', 'Date');
    data.addColumn('number', 'Usage');

    data.addRows([
[new Date(2015,04,09,20,00) , 591],
[new Date(2015,04,09,20,30) , 799],
[new Date(2015,04,09,21,00) , 717],
[new Date(2015,04,09,21,30) , 947]

    ]);

    var options = {
         width: 350,
         height: 200,
        hAxis: {
            title: 'Time',
        format: 'MM/dd/yy hh:mm'
        },
        vAxis: {
            title: 'MB'
        }
    };

    var chart = new google.visualization.LineChart(document.getElementById('chart_div'));

    chart.draw(data, options);
}

////////chart 4

//google.load('visualization', '1', {packages: ['corechart']});
google.setOnLoadCallback(drawBasic2);

function drawBasic2() {

    var data = new google.visualization.DataTable();
    data.addColumn('date', 'Date');
    data.addColumn('number', 'Usage');

    data.addRows([
[new Date(2015,04,09,20,00) , 591],
[new Date(2015,04,09,20,30) , 799],
[new Date(2015,04,09,21,00) , 717],
[new Date(2015,04,09,21,30) , 947]

    ]);

    var options = {
         width: 350,
         height: 200,
        hAxis: {
            title: 'Time',
        format: 'MM/dd/yy hh:mm'
        },
        vAxis: {
            title: 'HZ'
        }
    };

    var chart2 = new google.visualization.LineChart(document.getElementById('chart_div2'));

    chart2.draw(data, options);
}

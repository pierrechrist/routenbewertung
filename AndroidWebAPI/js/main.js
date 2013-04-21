//Tablesorter bei Seiten laden auf die Tabelle anwenden und wenn chart_div vorhanden das PieChart zeichnen
$(document).ready(function() 
    { 
        $("#myTable").tablesorter();
		if(document.getElementById('chart_div') != null) {
			drawChart();
		}
    } 
); 

//Bild in neuer Seite öffnen und Drucker Dialog aufrufen
function printImg() {
	pwin = window.open(document.getElementById("printI").src,"_blank");
	pwin.onload = function () {window.print();};
}

//Tabellen Click ausführen
function tableClick(page, id)
{
	window.open('admin.php?page='+page+'&id='+id,'_self',false);
}

//Delete Click ausführen
function deleteClick(func, id)
{
	var conf = confirm("Eintrag wirklich löschen?");
    if(conf == true){
		window.open('adminFunctions.php?function='+func+'&id='+id,'_self',false);
	}
}

//Datenbank reinigen
function cleanDB()
{
	var conf = confirm("Datenbank reinigen?");
    if(conf == true){
		window.open('adminFunctions.php?function=cleanDB','_self',false);
	}
}

// Load the Visualization API and the piechart package.
google.load('visualization', '1.0', {'packages':['corechart']});

// Callback that creates and populates a data table,
// instantiates the pie chart, passes in the data and
// draws it.
function drawChart() {

	// Create the data table.
	var flash =  parseInt($("#flash").text());
	var redpoint =  parseInt($("#redpoint").text());
	var project =  parseInt($("#project").text());
	
	var data = new google.visualization.DataTable();
	data.addColumn('string', 'Topping');
	data.addColumn('number', 'Slices');
	data.addRows([
	  ['Flash', flash],
	  ['Rotpunkt', redpoint],
	  ['Projekt', project],
	]);

	// Set chart options
	var options = {'title':'Routen Statistik',
				   'width':500,
				   'height':400};

	// Instantiate and draw our chart, passing in some options.
	var chart = new google.visualization.PieChart(document.getElementById('chart_div'));
	chart.draw(data, options);
}


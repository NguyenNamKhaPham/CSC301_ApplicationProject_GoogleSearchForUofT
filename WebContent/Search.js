$(document).ready(function(){
	$('#searchButton').click(function(){
		$('#search').css({
			'position': 'absolute',
			'top': '60px',
			'left': '350px',
			'display': 'block',
			'width': '60%'
		});

		$('h1').css({
			'position': 'absolute',
			'left': '50px',
			'top': '30px',
			'margin-right': '20px'
		});

		$('span').css({
			'display': 'none'
		});

		$('#searchQuery').css({
			'width': '80%',
		});

		$('#searchRes').css({
			'position': 'absolute',
			'top': '100px',
			'left': '50px',
			'width': '88%',
			'height': '80%'
		});

	});

});

document.addEventListener("DOMContentLoaded", function() {
	var input = document.querySelector('input[name="ingredientNames"]');
	if (!input) {
		console.error("El campo de entrada 'ingredientNames' no se encontró.");
		return;
	}
	var tagify = new Tagify(input, {
		whitelist: [], // Lista de ingredientes existentes (se llenará dinámicamente)
		enforceWhitelist: false, // Permite crear nuevos ingredients
		dropdown: {
			enabled: 1, // Muestra sugerencias automáticamente
			maxItems: 10, // Número máximo de sugerencias
		},
		delimiters: ",", // Permite escribir ingredientes separados por coma
		   trim: true,
		   originalInputValueFormat: values => values.map(ingredient => ingredient.value) // Solo envía los valores
	});

	// Configura la función para buscar sugerencias dinámicamente
	tagify.on('input', function(e) {
		const query = e.detail.value;

		if (query.length >= 2) {
			fetch(`/ingredients/suggest?query=${encodeURIComponent(query)}`)
				.then(response => response.json())
				.then(suggestions => {
					tagify.settings.whitelist = suggestions;
					tagify.dropdown.show.call(tagify);
				})
				.catch(error => console.error('Error al cargar sugerencias:', error));
		}
	});
});
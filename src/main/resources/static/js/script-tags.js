
document.addEventListener("DOMContentLoaded", function() {
	var input = document.querySelector('input[name="tagNames"]');
	if (!input) {
		console.error("El campo de entrada 'tagNames' no se encontró.");
		return;
	}
	var tagify = new Tagify(input, {
		whitelist: [], // Lista de tags existentes (se llenará dinámicamente)
		enforceWhitelist: false, // Permite crear nuevos tags
		dropdown: {
			enabled: 1, // Muestra sugerencias automáticamente
			maxItems: 10, // Número máximo de sugerencias
		},
		delimiters: ",", // Permite escribir tags separados por coma
		   trim: true,
		   originalInputValueFormat: values => values.map(tag => tag.value) // Solo envía los valores
	});

	// Configura la función para buscar sugerencias dinámicamente
	tagify.on('input', function(e) {
		const query = e.detail.value;

		if (query.length >= 2) {
			fetch(`/tags/suggest?query=${encodeURIComponent(query)}`)
				.then(response => response.json())
				.then(suggestions => {
					tagify.settings.whitelist = suggestions;
					tagify.dropdown.show.call(tagify);
				})
				.catch(error => console.error('Error al cargar sugerencias:', error));
		}
	});
});
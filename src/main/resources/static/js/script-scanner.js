document.addEventListener("DOMContentLoaded", () => {
    // No iniciar el escáner automáticamente, solo cuando se abra el modal
    const html5QrCode = new Html5Qrcode("barcode-scanner");
    let isScannerStarted = false;

    // Configuración del escáner
    const config = {
        fps: 10,  // 10 frames por segundo
        qrbox: {
            width: Math.min(window.innerWidth, 300),  // Ajustar el area de escaneo dinámicamente
            height: Math.min(window.innerHeight, 200)  // Ajustar el area de escaneo dinámicamente
        }
    };

    // Función para iniciar el escáner cuando se abre el modal
    const startScanner = () => {
        if (!isScannerStarted) {
            html5QrCode.start(
                { facingMode: "environment" }, // Cámara trasera
                config,
                (decodedText, decodedResult) => {
                    document.getElementById("resultRead").innerText = decodedText;
                    document.getElementById('barcode-scanner').classList.add('scanned-box');
                    document.getElementById("barcodeInput").value = decodedText;
                    html5QrCode.stop().then(() => {
                        document.getElementById("submitButton").click();
                    }).catch((err) => {
                        console.error("Error al detener el escáner:", err);
                    });
                },
                (errorMessage) => {
                    document.getElementById('barcode-scanner').classList.remove('scanned-box');
                }
            ).catch((err) => {
                console.error("No se pudo iniciar el escáner:", err);
            });

            isScannerStarted = true;  // Marcar que el escáner ha sido iniciado
        }
    };

    // Añadir evento para abrir el modal y activar el escáner
    const agregarProductoButton = document.querySelector('[data-bs-target="#agregarProductoModal"]');
    const modal = new bootstrap.Modal(document.getElementById('agregarProductoModal'));

    agregarProductoButton.addEventListener('click', () => {
        // Cuando se hace clic en el botón para agregar un producto, mostramos el modal
        modal.show();
		// Iniciamos el escáner
        startScanner();
    });

    // Cuando se cierra el modal, detenemos el escáner (si está activo)
    document.getElementById('agregarProductoModal').addEventListener('hidden.bs.modal', () => {
        if (isScannerStarted) {
            html5QrCode.stop();
            isScannerStarted = false;
        }
    });
});

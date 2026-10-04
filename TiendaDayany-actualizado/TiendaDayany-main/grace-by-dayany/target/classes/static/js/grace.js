/* Grace by Dayany — comportamiento del sitio */
(function () {
    'use strict';

    var $ = function (s, r) { return (r || document).querySelector(s); };
    var $$ = function (s, r) { return Array.prototype.slice.call((r || document).querySelectorAll(s)); };

    /* ----- Aviso emergente ----- */
    var toastTimer;
    function toast(msg) {
        var t = $('#toast');
        if (!t) return;
        t.textContent = msg;
        t.classList.add('ver');
        clearTimeout(toastTimer);
        toastTimer = setTimeout(function () { t.classList.remove('ver'); }, 2400);
    }

    /* ----- Favoritos (se guardan en este navegador) ----- */
    var FAV_KEY = 'grace_favs';
    function leerFavs() {
        try { return JSON.parse(localStorage.getItem(FAV_KEY) || '[]'); } catch (e) { return []; }
    }
    function guardarFavs(l) {
        try { localStorage.setItem(FAV_KEY, JSON.stringify(l)); } catch (e) { /* sin almacenamiento */ }
    }
    function pintarFavs() {
        var favs = leerFavs();
        $$('[data-fav]').forEach(function (b) {
            var on = favs.indexOf(String(b.dataset.fav)) !== -1;
            b.classList.toggle('on', on);
            var use = $('use', b);
            if (use) use.setAttribute('href', on ? '#i-heart-fill' : '#i-heart');
        });
    }
    document.addEventListener('click', function (e) {
        var b = e.target.closest('[data-fav]');
        if (!b) return;
        e.preventDefault();
        var id = String(b.dataset.fav);
        var favs = leerFavs();
        var i = favs.indexOf(id);
        if (i === -1) { favs.push(id); toast('Agregado a favoritos'); }
        else { favs.splice(i, 1); toast('Quitado de favoritos'); }
        guardarFavs(favs);
        pintarFavs();
    });
    pintarFavs();

    /* ----- Página de favoritos ----- */
    var lista = $('#fav-lista');
    if (lista) {
        var ids = leerFavs();
        if (!ids.length) { $('#fav-vacio').hidden = false; }
        else {
            fetch(lista.dataset.url + '?ids=' + ids.join(','))
                .then(function (r) { return r.text(); })
                .then(function (html) {
                    lista.innerHTML = html;
                    if (!lista.children.length) $('#fav-vacio').hidden = false;
                    pintarFavs();
                });
        }
    }

    /* ----- Agregar al carrito sin recargar (tarjetas) ----- */
    document.addEventListener('submit', function (e) {
        var f = e.target;
        if (!f.classList || !f.classList.contains('js-add') || !window.fetch) return;
        e.preventDefault();
        fetch(f.dataset.ajax, { method: 'POST', body: new URLSearchParams(new FormData(f)) })
            .then(function (r) { if (!r.ok) throw new Error(); return r.text(); })
            .then(function (n) {
                var badge = $('#cart-badge');
                if (badge) badge.textContent = n;
                toast('Agregado al carrito');
            })
            .catch(function () { f.submit(); });
    });

    /* ----- Filtros del catálogo: aplicar al cambiar ----- */
    var ff = $('#form-filtros');
    if (ff) {
        var btn = $('.btn-aplicar', ff);
        if (btn) btn.hidden = true;
        ff.addEventListener('change', function () { ff.submit(); });
    }
    var fo = $('#form-orden');
    if (fo) fo.addEventListener('change', function () { fo.submit(); });

    /* ----- Página de producto ----- */
    var fp = $('#form-prod');
    if (fp) {
        var qi = $('input[name=cantidad]', fp);
        $$('[data-q]', fp).forEach(function (b) {
            b.addEventListener('click', function () {
                var v = (parseInt(qi.value, 10) || 1) + parseInt(b.dataset.q, 10);
                qi.value = Math.min(10, Math.max(1, v));
            });
        });

        // Galería (ciclo de fotos)
        var minis = $$('.mini'), puntos = $$('.puntos i'), actual = 0;
        function ir(n) {
            actual = (n + minis.length) % minis.length;
            minis.forEach(function (m, i) { m.classList.toggle('activa', i === actual); });
            puntos.forEach(function (p, i) { p.classList.toggle('activa', i === actual); });
        }
        minis.forEach(function (m) { m.addEventListener('click', function () { ir(parseInt(m.dataset.i, 10)); }); });
        var pv = $('#g-prev'), nx = $('#g-next');
        if (pv) pv.addEventListener('click', function () { ir(actual - 1); });
        if (nx) nx.addEventListener('click', function () { ir(actual + 1); });

        // Cuenta regresiva hasta las 6:00 p. m. (o hasta mañana a esa hora)
        var cuenta = $('#cuenta');
        if (cuenta) {
            var pad = function (n) { return String(n).padStart(2, '0'); };
            var tick = function () {
                var ahora = new Date(), fin = new Date();
                fin.setHours(18, 0, 0, 0);
                if (fin <= ahora) fin.setDate(fin.getDate() + 1);
                var s = Math.floor((fin - ahora) / 1000);
                cuenta.textContent = pad(Math.floor(s / 3600)) + ':' + pad(Math.floor(s % 3600 / 60)) + ':' + pad(s % 60);
            };
            tick();
            setInterval(tick, 1000);
        }
    }
})();

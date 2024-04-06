/*
 * heart-check
 * Copyright (C) 2024  ykkz000
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

String.prototype.format = function () {
    let args = arguments;
    return this.replace(/{(\d+)}/g, function (match, number) {
        return typeof args[number] != 'undefined' ? args[number] : match;
    });
};
layui.define(function (exports) {
    exports('$', $);
});
layui.define(function (exports) {
    let $ = layui.$;
    $.i18n.loadLang = function (lang) {
        $.i18n.properties({
            name: 'text',
            path: 'assets/lang/',
            mode: 'map',
            language: lang,
            cache: true,
            encoding: 'UTF-8',
            callback: function () {
                $('body').find('*').each(function () {
                    if ($(this).attr('data-i18n')) {
                        $(this).text($.i18n.prop($(this).attr('data-i18n')));
                    }
                    if ($(this).attr('data-i18n-title')) {
                        $(this).prop('title', $.i18n.prop($(this).attr('data-i18n-title')));
                    }
                    if ($(this).attr('data-i18n-placeholder')) {
                        $(this).prop('placeholder', $.i18n.prop($(this).attr('data-i18n-placeholder')));
                    }
                });
            }
        });
    }
    exports('i18n', $.i18n);
});
$("document").ready(function () {
    $(".header").load("components/header.html");
    $(".footer").load("components/footer.html");
});

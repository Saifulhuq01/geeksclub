#!/bin/sh

# Replace the env.js file with runtime values
cat <<EOF > /usr/share/nginx/html/assets/env.js
(function (window) {
  window['env'] = window['env'] || {};
  window['env']['apiUrl'] = '${API_BASE_URL:-http://localhost:8080}';
})(this);
EOF

exec "$@"

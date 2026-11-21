#!/usr/bin/env bash
# Generates the prototype's photos with Codex's built-in image generation.
# Usage: tools/gen-images.sh [name ...]   (all missing ones by default); results in assets/raw/<name>.png
set -uo pipefail
cd "$(dirname "$0")/.."
CODEX="${CODEX:-$HOME/.local/share/zed/external_agents/registry/npx/codex-acp/node_modules/@openai/codex-linux-x64/vendor/x86_64-unknown-linux-musl/bin/codex}"
STYLE="Photorealistic professional travel photography, natural light, rich deep navy and azure blue tones, tropical bay of Nha Trang, Vietnam with green hilly islands on the horizon. No text, no letters, no logos, no watermarks."
gen() {
  local name="$1" shape="$2" prompt="$3" out="$PWD/assets/raw/$1.png"
  [ -f "$out" ] && [ -z "${FORCE:-}" ] && return 0
  local dir; dir=$(mktemp -d)
  ( cd "$dir" && timeout 600 "$CODEX" exec --skip-git-repo-check -s workspace-write --ephemeral --add-dir "$(dirname "$out")" \
    "Use your built-in image generation tool to generate exactly ONE image, $shape. Subject: $prompt $STYLE Then copy the generated PNG to $out and reply with that path only." \
    > "$OLDPWD/assets/logs/$name.log" 2>&1 )
  [ -f "$out" ] && echo "ok   $name" || echo "FAIL $name"
}
export -f gen; export CODEX STYLE FORCE="${FORCE:-}"
LIST=$(cat <<'P'
hero_deck|portrait 2:3|View from the cockpit of a sailing yacht along its deck toward the bow at golden sunset, large white sail and mast on the right side, sun low over the sea near the horizon on the right, calm dark blue water; the upper left part of the frame is calm dark blue dusk sky with no details (space for a headline). No people.
deck_sunset|landscape 3:2|View from the foredeck of a sailing yacht toward the bow at sunset, white sail and rigging on the right, glowing orange sun just above the sea horizon, islands silhouettes, teak and white deck. No people.
deck_day|landscape 3:2|View from the side deck of a sailing yacht heeling under sail on a bright sunny day, white sails and stainless rails on the right, white wake foam on deep blue water, green mountainous coast in the distance. No people.
sea_dusk|landscape 3:2|Calm open sea at blue hour dusk, dark navy sky filling the upper two thirds, a thin warm glow on the horizon, low silhouettes of islands on the right, minimal and quiet. No boats, no people.
yacht_sunset|landscape 3:2|A sailing yacht with full sails seen from the side, backlit silhouette against a warm orange sunset sky, calm sea with golden reflections. No people visible.
yacht_cruiser|landscape 3:2|A modern 40-foot white cruising sailing yacht under mainsail and genoa, three-quarter front view, sunny day, turquoise and blue water, light clouds. No people visible.
yacht_keelboat|landscape 3:2|A small modern 19-foot sport keelboat with white hull and white sails, sailing fast and slightly heeled on a sunny day, seen from the side, blue sea. Crew of two tiny distant figures in sailing gear, faces not visible.
yacht_dinghy|landscape 3:2|A small modern sailing dinghy with a white hull and a white and blue sail gliding near the shore in warm evening light, calm water. Two tiny distant sailors, faces not visible.
winch|landscape 3:2|Close-up of a polished stainless steel sailing winch with a rope wrapped around it on the white deck of a yacht, blurred blue sea and coast in the background, shallow depth of field.
knot|landscape 3:2|Close-up of a thick white nautical rope tied in a neat knot on a yacht, blurred deep blue sea background, shallow depth of field.
sails_sunset|landscape 3:2|Looking along the deck up at the white sails and mast of a sailing yacht at sunset, warm orange light through the sails, sea horizon on the left.
coast|landscape 3:2|Aerial view of a rocky green coastline with cliffs and a deep blue bay, clear turquoise water near the rocks, sunny day; the left half is mostly open dark blue water. No boats, no people.
gift|landscape 3:2|A gift box wrapped in light grey paper with a brown satin ribbon bow lying on the dark wooden deck of a yacht next to a coiled rope, moody dark background on the left half, soft warm light, premium feel.
instructor|square 1:1|Head-and-shoulders portrait of a friendly male sailing instructor about 35 years old with short beard, navy cap and sunglasses, white technical sailing jacket, standing on a yacht, blurred sea background, looking to the side.
avatar|square 1:1|Head-and-shoulders portrait of a smiling man about 30 years old with short dark hair in a navy polo shirt on a marina pier, blurred yachts and sea behind, soft daylight, looking at the camera.
P
)
if [ $# -gt 0 ]; then LIST=$(grep -E "^($(IFS='|'; echo "$*"))\|" <<<"$LIST"); fi
echo "$LIST" | xargs -d '\n' -P "${JOBS:-4}" -I{} bash -c 'IFS="|" read -r n s p <<<"$1"; gen "$n" "$s" "$p"' _ {}

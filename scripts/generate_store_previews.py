"""Generate 1920x1080 app store preview images from game screenshots."""

from __future__ import annotations

import os
from pathlib import Path

from PIL import Image, ImageDraw, ImageFilter, ImageFont

ROOT = Path(__file__).resolve().parents[1]
ASSETS = Path(
    r"C:\Users\immak\.cursor\projects\e-Programming-makzaprojects-mindgames-Findapair\assets"
)
OUTPUT = ROOT / "store-previews"

CANVAS_W, CANVAS_H = 1920, 1080

PREVIEWS = [
    {
        "screenshot": "c__Users_immak_AppData_Roaming_Cursor_User_workspaceStorage_b27d9bd6686f2bf0e89b80a8a228eb3a_images_Screenshot_20260702_105956-af50bd9f-23d5-4314-9a32-76acd9eae31a.png",
        "background": "background_menu_oasis.png",
        "caption": "ВЫБЕРИ СВОЙ ПУТЬ ЧЕРЕЗ ПИРАМИДЫ",
        "subtitle": "Сотни уровней в мире древнего Египта",
        "output": "01_level_select.png",
        "phone_x": 0.58,
    },
    {
        "screenshot": "c__Users_immak_AppData_Roaming_Cursor_User_workspaceStorage_b27d9bd6686f2bf0e89b80a8a228eb3a_images_Screenshot_20260702_110137-27cdd320-7c2f-42c8-adca-9542972ffa1d.png",
        "background": "background_pyramides_day.png",
        "caption": "НАЙДИ ВСЕ ПАРЫ РЕЛИКВИЙ",
        "subtitle": "Тренируй память на берегах Нила",
        "output": "02_pairs_gameplay.png",
        "phone_x": 0.42,
    },
    {
        "screenshot": "c__Users_immak_AppData_Roaming_Cursor_User_workspaceStorage_b27d9bd6686f2bf0e89b80a8a228eb3a_images_Screenshot_20260702_110223-9239f6b7-3590-4768-b4ff-284f5eb2b830.png",
        "background": "background_pyramides_scary.png",
        "caption": "ИСПЫТАЙ ПАМЯТЬ ФАРАОНОВ",
        "subtitle": "Собери четвёрки священных символов",
        "output": "03_quartets_gameplay.png",
        "phone_x": 0.58,
    },
    {
        "screenshot": "c__Users_immak_AppData_Roaming_Cursor_User_workspaceStorage_b27d9bd6686f2bf0e89b80a8a228eb3a_images_Screenshot_20260702_110249-a0e84d11-5ae1-4f99-a024-3059d6079702.png",
        "background": "background_pyramides_dark.png",
        "caption": "СОБЕРИ ТРИО СИМВОЛОВ",
        "subtitle": "Новый режим — три одинаковых артефакта",
        "output": "04_trios_gameplay.png",
        "phone_x": 0.42,
    },
    {
        "screenshot": "c__Users_immak_AppData_Roaming_Cursor_User_workspaceStorage_b27d9bd6686f2bf0e89b80a8a228eb3a_images_Screenshot_20260702_110351-2e9007ce-19b4-4904-a2ab-0d7c824884c0.png",
        "background": "background_pyramides_dark.png",
        "caption": "РАСКРОЙ ТАЙНЫ НОЧНОГО ЕГИПТА",
        "subtitle": "Загадочные уровни под звёздным небом",
        "output": "05_trios_night.png",
        "phone_x": 0.58,
    },
]


def load_font(size: int, bold: bool = True) -> ImageFont.FreeTypeFont | ImageFont.ImageFont:
    candidates = [
        r"C:\Windows\Fonts\arialbd.ttf",
        r"C:\Windows\Fonts\impact.ttf",
        r"C:\Windows\Fonts\segoeuib.ttf",
        r"C:\Windows\Fonts\arial.ttf",
    ]
    if not bold:
        candidates = [
            r"C:\Windows\Fonts\segoeui.ttf",
            r"C:\Windows\Fonts\arial.ttf",
        ] + candidates
    for path in candidates:
        if os.path.exists(path):
            return ImageFont.truetype(path, size)
    return ImageFont.load_default()


def cover_resize(img: Image.Image, target_w: int, target_h: int) -> Image.Image:
    src_w, src_h = img.size
    scale = max(target_w / src_w, target_h / src_h)
    new_w = int(src_w * scale)
    new_h = int(src_h * scale)
    resized = img.resize((new_w, new_h), Image.Resampling.LANCZOS)
    left = (new_w - target_w) // 2
    top = (new_h - target_h) // 2
    return resized.crop((left, top, left + target_w, top + target_h))


def add_vignette(img: Image.Image) -> Image.Image:
    overlay = Image.new("RGBA", img.size, (0, 0, 0, 0))
    draw = ImageDraw.Draw(overlay)
    for y in range(img.height):
        alpha = int(90 * (y / img.height) ** 1.5)
        draw.line([(0, y), (img.width, y)], fill=(0, 0, 0, alpha))
    for y in range(220):
        alpha = int(140 * (1 - y / 220))
        draw.line([(0, y), (img.width, y)], fill=(0, 0, 0, alpha))
    return Image.alpha_composite(img.convert("RGBA"), overlay)


def rounded_mask(size: tuple[int, int], radius: int) -> Image.Image:
    mask = Image.new("L", size, 0)
    draw = ImageDraw.Draw(mask)
    draw.rounded_rectangle((0, 0, size[0], size[1]), radius=radius, fill=255)
    return mask


def place_phone(canvas: Image.Image, screenshot: Image.Image, phone_x_ratio: float) -> None:
    phone_h = 860
    phone_w = int(phone_h * screenshot.width / screenshot.height)
    radius = 28
    bezel = 14

    shot = screenshot.resize((phone_w, phone_h), Image.Resampling.LANCZOS)
    frame_w = phone_w + bezel * 2
    frame_h = phone_h + bezel * 2

    shadow = Image.new("RGBA", (frame_w + 80, frame_h + 80), (0, 0, 0, 0))
    shadow_draw = ImageDraw.Draw(shadow)
    shadow_draw.rounded_rectangle(
        (30, 30, frame_w + 30, frame_h + 30),
        radius=radius + 8,
        fill=(0, 0, 0, 170),
    )
    shadow = shadow.filter(ImageFilter.GaussianBlur(18))

    frame = Image.new("RGBA", (frame_w, frame_h), (0, 0, 0, 0))
    frame_draw = ImageDraw.Draw(frame)
    frame_draw.rounded_rectangle(
        (0, 0, frame_w - 1, frame_h - 1),
        radius=radius + 4,
        fill=(40, 28, 12, 255),
        outline=(212, 168, 67, 255),
        width=4,
    )

    inner_mask = rounded_mask((phone_w, phone_h), radius)
    inner = Image.new("RGBA", (phone_w, phone_h))
    inner.paste(shot, (0, 0), inner_mask)

    frame.paste(inner, (bezel, bezel), inner)

    x = int(CANVAS_W * phone_x_ratio - frame_w / 2)
    y = int((CANVAS_H - frame_h) / 2 + 50)

    canvas.alpha_composite(shadow, (x - 30, y - 20))
    canvas.alpha_composite(frame, (x, y))


def draw_caption_block(
    canvas: Image.Image,
    caption: str,
    subtitle: str,
    align_left: bool,
) -> None:
    draw = ImageDraw.Draw(canvas)
    title_font = load_font(62, bold=True)
    sub_font = load_font(34, bold=False)

    if align_left:
        x = 80
    else:
        bbox = draw.textbbox((0, 0), caption, font=title_font)
        text_w = bbox[2] - bbox[0]
        x = CANVAS_W - text_w - 80

    banner_h = 170
    banner = Image.new("RGBA", (CANVAS_W, banner_h), (0, 0, 0, 0))
    banner_draw = ImageDraw.Draw(banner)
    banner_draw.rectangle((0, 0, CANVAS_W, banner_h), fill=(45, 12, 8, 185))
    banner_draw.line([(0, banner_h - 2), (CANVAS_W, banner_h - 2)], fill=(212, 168, 67, 220), width=3)
    canvas.alpha_composite(banner, (0, 0))

    title_y = 34
    draw.text((x, title_y), caption, font=title_font, fill=(255, 214, 79, 255))
    draw.text((x, title_y + 72), subtitle, font=sub_font, fill=(255, 236, 196, 230))

    # Decorative wing accents
    accent_color = (212, 168, 67, 200)
    if align_left:
        draw.polygon([(x - 20, 55), (x - 70, 30), (x - 70, 80)], fill=accent_color)
        draw.polygon([(x - 20, 95), (x - 70, 70), (x - 70, 120)], fill=accent_color)
    else:
        end_x = x + draw.textbbox((0, 0), caption, font=title_font)[2]
        draw.polygon([(end_x + 20, 55), (end_x + 70, 30), (end_x + 70, 80)], fill=accent_color)
        draw.polygon([(end_x + 20, 95), (end_x + 70, 70), (end_x + 70, 120)], fill=accent_color)


def build_preview(item: dict) -> Image.Image:
    bg = Image.open(ASSETS / item["background"]).convert("RGBA")
    shot = Image.open(ASSETS / item["screenshot"]).convert("RGBA")

    canvas = cover_resize(bg, CANVAS_W, CANVAS_H)
    canvas = add_vignette(canvas)

    phone_on_right = item["phone_x"] > 0.5
    place_phone(canvas, shot, item["phone_x"])
    draw_caption_block(canvas, item["caption"], item["subtitle"], align_left=not phone_on_right)

    return canvas.convert("RGB")


def main() -> None:
    OUTPUT.mkdir(parents=True, exist_ok=True)
    for item in PREVIEWS:
        out_path = OUTPUT / item["output"]
        preview = build_preview(item)
        preview.save(out_path, "PNG", optimize=True)
        print(f"Saved {out_path} ({preview.size[0]}x{preview.size[1]})")


if __name__ == "__main__":
    main()

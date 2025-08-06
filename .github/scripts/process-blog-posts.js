const matter = require('gray-matter');
const fs = require('fs');
const path = require('path');

const changedFiles = process.argv.slice(2);

if (changedFiles.length === 0) {
    console.log('No blog posts to process');
    process.exit(0);
}

console.log('Processing blog posts:', changedFiles);

async function sendDiscordNotification(post) {
    const webhookUrl = process.env.DISCORD_WEBHOOK_URL;

    if (!webhookUrl) {
        console.log('⚠️ DISCORD_WEBHOOK_URL not set, skipping Discord notification');
        return;
    }

    let formattedDate = 'Today';
    if (post.date) {
        try {
            const date = new Date(post.date);
            formattedDate = date.toLocaleDateString('en-US', {
                year: 'numeric',
                month: 'long',
                day: 'numeric'
            });
        } catch (e) {
            formattedDate = post.date;
        }
    }

    const embed = {
        embeds: [{
            title: post.pinned
                ? `📌 **PINNED** • ${post.title}`
                : `📝 **NEW BLOG POST** • ${post.title}`,
            description: post.description || 'No description available',
            url: `https://polocloud.de/blog/${post.slug}`,
            color: post.pinned ? 0xFFD700 : 0x5865F2, // Gold for pinned, Discord Blue for normal
            thumbnail: {
                url: 'https://polocloud.de/logo.png'
            },
            fields: [
                {
                    name: '👤 **Author**',
                    value: post.author || 'PoloCloud Team',
                    inline: true
                },
                {
                    name: '📅 **Published**',
                    value: formattedDate,
                    inline: true
                },
                {
                    name: '🔗 **Read More**',
                    value: `[View on Website](https://polocloud.de/blog/${post.slug})`,
                    inline: true
                }
            ],
            footer: {
                text: 'PoloCloud Blog • Stay updated with the latest news',
                icon_url: 'https://polocloud.de/logo.png'
            },
            timestamp: new Date().toISOString(),
            image: {
                url: 'https://images.unsplash.com/photo-1518709268805-4e9042af2176?w=800&h=200&fit=crop&crop=center'
            }
        }]
    };

    if (post.tags && post.tags.length > 0) {
        const tagEmojis = ['🏷️', '📌', '🎯', '⭐', '🔥', '💡', '🚀', '🎉'];
        const formattedTags = post.tags.map((tag, index) => {
            const emoji = tagEmojis[index % tagEmojis.length];
            return `${emoji} \`${tag}\``;
        }).join(' ');

        embed.embeds[0].fields.push({
            name: '🏷️ **Tags**',
            value: formattedTags,
            inline: false
        });
    }

    embed.embeds[0].fields.push({
        name: '━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━',
        value: '**Ready to dive in? Click the link above to read the full article!** 📖',
        inline: false
    });

    try {
        const response = await fetch(webhookUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(embed),
        });

        if (!response.ok) {
            throw new Error(`Discord webhook failed: ${response.statusText}`);
        }

        console.log(`✅ Discord notification sent for: ${post.title}`);
    } catch (error) {
        console.error(`❌ Failed to send Discord notification for ${post.title}:`, error.message);
    }
}

async function processBlogPosts() {
    for (const file of changedFiles) {
        try {
            if (!fs.existsSync(file)) {
                console.log(`⚠️ File not found: ${file}`);
                continue;
            }

            const fileContent = fs.readFileSync(file, 'utf8');
            const { data } = matter(fileContent);

            const relativePath = file.replace(/^docs\/content\/blog\//, '').replace(/\.mdx$/, '');
            const slug = relativePath;

            const post = {
                title: data.title || 'Untitled',
                description: data.description || '',
                date: data.date || '',
                author: data.author || 'PoloCloud Team',
                tags: data.tags || [],
                slug,
                pinned: data.pinned || false,
            };

            console.log(`📝 Processing: ${post.title}`);
            await sendDiscordNotification(post);

        } catch (error) {
            console.error(`❌ Error processing ${file}:`, error.message);
        }
    }
}

processBlogPosts().catch(error => {
    console.error('❌ Fatal error:', error.message);
    process.exit(1);
});
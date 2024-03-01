import {themes as prismThemes} from 'prism-react-renderer';
import type {Config} from '@docusaurus/types';
import type * as Preset from '@docusaurus/preset-classic';

const config: Config = {
  title: 'Welcome!',
  tagline: 'Connect to our APIs and start to Develop.',
  favicon: 'img/favicon.ico',

  // Set the production url of your site here
  url: 'https://apidocs.dev.qhrtech.com/',
  // Set the /<baseUrl>/ pathname under which your site is served
  // For GitHub pages deployment, it is often '/<projectName>/'
  baseUrl: 'apiguide',

  // GitHub pages deployment config.
  // If you aren't using GitHub pages, you don't need these.
  organizationName: 'Qhr Technologies', // Usually your GitHub org/user name.
  projectName: 'Accuro Api', // Usually your repo name.

  onBrokenLinks: 'throw',
  onBrokenMarkdownLinks: 'warn',

  // Even if you don't use internationalization, you can use this field to set
  // useful metadata like html lang. For example, if your site is Chinese, you
  // may want to replace "en" with "zh-Hans".
  i18n: {
    defaultLocale: 'en',
    locales: ['en'],
  },

  markdown: {
    mermaid: true,
  },
  themes: ['@docusaurus/theme-mermaid',[
      require.resolve("@easyops-cn/docusaurus-search-local"),
      /** @type {import("@easyops-cn/docusaurus-search-local").PluginOptions} */
      ({
        // ... Your options.
        // `hashed` is recommended as long-term-cache of index file is possible.
        hashed: true,
        // For Docs using Chinese, The `language` is recommended to set to:
        // ```
        // language: ["en", "zh"],
        // ```
      }),
    ],],

  presets: [
    [
      'classic',
      {
        docs: {
          sidebarPath: './sidebars.ts',
          // Please change this to your repo.
          // Remove this to remove the "edit this page" links.
          /*editUrl:
            'https://github.com/facebook/docusaurus/tree/main/packages/create-docusaurus/templates/shared/',*/
        },
        blog: {
          showReadingTime: true,
          // Please change this to your repo.
          // Remove this to remove the "edit this page" links.
          /*editUrl:
            'https://github.com/facebook/docusaurus/tree/main/packages/create-docusaurus/templates/shared/',*/
        },
        theme: {
          customCss: './src/css/custom.css',
        },
      } satisfies Preset.Options,
    ],
  ],

  plugins: [
  ],

  themeConfig: {
    // Replace with your project's social card
    image: 'img/docusaurus-social-card.jpg',
    docs: {
      sidebar: {
        hideable: true,
        autoCollapseCategories: true,
      },
    },
    navbar: {
      title: '',
      logo: {
        alt: 'My Guide',
        src: 'img/icons8-document.svg',
      },
      hideOnScroll: true,
      items: [
        {
          type: 'docSidebar',
          sidebarId: 'tutorialSidebar',
          position: 'left',
          label: 'Guide v1.0',
        },
        {
          type: 'search',
          position: 'right',
        },
        {
          type: 'localeDropdown',
          position: 'right',
        },
        /* {
           type: 'doc',
           docId: 'faq/FAQ',
           label: 'FAQ',
           position: 'left',
         },*/
        /* { to: '/blog', label: 'Blog', position: 'left' },*/
        /* {
           href: 'https://github.com/facebook/docusaurus',
           label: 'GitHub',
           position: 'right',
         },*/

      ],
    },
    footer: {
      style: 'dark',
      logo: {
        alt: 'Meta Open Source Logo',
        src: 'img/QHR Logo-White.png',
        //href: 'https://opensource.fb.com',
        width: 120,
        height: 50,
      },
      links: [
        /* {
           title: 'Docs',
           items: [
             {
               label: 'Accuro Guide',
               to: '/docs/intro',
             },
           ],
         },*/
        /* {
           title: 'Community',
           items: [
             {
               label: 'Stack Overflow',
               href: 'https://stackoverflow.com/questions/tagged/docusaurus',
             },
             {
               label: 'Discord',
               href: 'https://discordapp.com/invite/docusaurus',
             },
             {
               label: 'Twitter',
               href: 'https://twitter.com/docusaurus',
             },
           ],
         },*/
        /* {
           title: 'More',
           items: [
             {
               label: 'Blog',
               to: '/blog',
             },
             {
               label: 'GitHub',
               href: 'https://github.com/facebook/docusaurus',
             },
           ],
         },*/
      ],
      copyright: `Copyright © ${new Date().getFullYear()} All Rights Reserved.`,
    },
    prism: {
      theme: prismThemes.github,
      darkTheme: prismThemes.dracula,
    },
  } satisfies Preset.ThemeConfig,
};

export default config;

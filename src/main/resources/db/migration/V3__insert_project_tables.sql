INSERT INTO project_status (
    name,
    description
) VALUES
('Em planejamento', 'Ainda está formando a equipe e se organizando'),
('Em andamento', 'Já está organizado e começou a criação'),
('Fase de testes', 'Projeto está na reta final, mas ainda está fazendo lapidações'),
('Concluído', 'Já é possivel acessar o projeto');

INSERT INTO project_complexity (
   name,
   description,
   hex_color
) VALUES
('Baixa', 'Projeto de complexidade geral baixa', '#80EF80'),
('Média', 'Projeto de complexidade geral mediana', '#FFEE8C'),
('Alta', 'Projeto de complexidade geral alta', '#FFC067'),
('Muito Alta', 'Projeto de complexidade geral muito alta', '#FF2C2C');

INSERT INTO project_topics (
   name,
   description,
   hex_color
) VALUES
-- Desenvolvimento de Software
('Aplicações Web', 'Projetos voltados ao desenvolvimento de sites e sistemas web.', '#4A90E2'),
('Aplicações Mobile', 'Aplicativos para dispositivos Android, iOS ou multiplataforma.', '#50E3C2'),
('APIs', 'Serviços e interfaces de comunicação entre sistemas.', '#6D6D6D'),
('Ferramentas para Desenvolvedores', 'Extensões, bibliotecas ou apps que ajudam programadores.', '#1C1C1C'),

-- Jogos e Interativos
('Desenvolvimento de Jogos', 'Projetos de jogos digitais em 2D ou 3D.', '#9B51E0'),
('Narrativas Interativas', 'Histórias digitais com escolhas e ramificações.', '#F2994A'),
('Game Jams', 'Projetos criados em eventos colaborativos de curta duração.', '#BB6BD9'),

-- Criatividade e Arte
('Música e Áudio', 'Projetos relacionados à criação sonora, trilhas e efeitos.', '#27AE60'),
('Arte Digital', 'Criação de ilustrações, sprites, ícones e outros visuais.', '#EB5757'),
('Animação', 'Produções visuais animadas em 2D ou 3D.', '#F2C94C'),

-- Inteligência Artificial e Dados
('Inteligência Artificial', 'Soluções com aprendizado de máquina, redes neurais, etc.', '#2D9CDB'),
('Visão Computacional', 'Projetos com análise de imagens e vídeos por IA.', '#56CCF2'),
('Processamento de Linguagem Natural', 'Análise, geração ou tradução automática de texto.', '#B76FFB'),
('Ciência de Dados', 'Exploração, visualização e modelagem de dados.', '#27AE60'),

-- Conteúdo e Educação
('Educação', 'Projetos com foco em ensino, aprendizado e didática.', '#F2994A'),
('Documentação', 'Criação de conteúdo técnico e guias explicativos.', '#F2F2F2'),
('Escrita Criativa', 'Textos, roteiros, histórias e conteúdo literário.', '#C0392B'),

-- Sociedade e Comunidade
('Saúde e Bem-estar', 'Soluções voltadas à saúde mental, física e autocuidado.', '#E67E22'),
('Meio Ambiente', 'Projetos relacionados à sustentabilidade e natureza.', '#219653'),
('Tecnologia Social', 'Ferramentas para impacto social e inclusão digital.', '#6FCF97'),
('Diversidade e Inclusão', 'Iniciativas que promovem representatividade e equidade.', '#9B51E0'),

-- Gestão e Produtividade
('Organização Pessoal', 'Apps e ferramentas para produtividade e rotina.', '#34495E'),
('Colaboração em Equipe', 'Soluções para trabalho em grupo e gestão de times.', '#2ECC71'),
('Automação', 'Projetos que otimizam tarefas repetitivas.', '#7F8C8D'),

-- Web3 e Futuro da Web
('Blockchain', 'Soluções descentralizadas baseadas em blockchain.', '#F39C12'),
('Criptoativos e NFTs', 'Projetos relacionados a tokens e ativos digitais.', '#8E44AD'),
('Metaverso', 'Experiências imersivas e mundos virtuais.', '#2980B9'),

-- Outros / Multidisciplinares
('Open Source', 'Projetos abertos com foco em colaboração da comunidade.', '#000000'),
('Projetos Experimentais', 'Iniciativas inovadoras ou conceituais.', '#BDC3C7'),
('Acessibilidade', 'Soluções inclusivas para diferentes tipos de usuários.', '#E0C341');


INSERT INTO project_roles (
    name,
    description
) VALUES
-- Desenvolvimento
('Desenvolvedor Frontend', 'Responsável por implementar interfaces e experiências visuais no navegador.'),
('Desenvolvedor Backend', 'Cria e mantém a lógica do servidor, bancos de dados e APIs.'),
('Desenvolvedor Fullstack', 'Atua tanto no frontend quanto no backend, com conhecimento geral da aplicação.'),
('Desenvolvedor Mobile', 'Desenvolve aplicações para dispositivos móveis (Android/iOS).'),
('Desenvolvedor de Jogos', 'Implementa a lógica de jogos, física, animações e interações.'),

-- Design
('Designer de Interface (UI)', 'Cria a aparência visual da interface do usuário.'),
('Designer de Experiência (UX)', 'Foca na usabilidade e experiência do usuário.'),
('Designer de Jogos', 'Define mecânicas, regras e interações em jogos digitais.'),
('Designer Gráfico', 'Cria elementos visuais como logotipos, ilustrações e layouts.'),
('Designer de Animação (Motion)', 'Cria transições, animações e interações visuais.'),

-- Conteúdo e Escrita
('Redator Técnico', 'Produz documentação, guias técnicos e tutoriais.'),
('Redator Criativo', 'Escreve textos narrativos, promocionais ou descritivos.'),
('Criador de Conteúdo', 'Produz vídeos, postagens e materiais digitais para divulgação.'),

-- Dados e Inteligência Artificial
('Cientista de Dados', 'Analisa dados e cria modelos preditivos e estatísticos.'),
('Engenheiro de Machine Learning', 'Desenvolve sistemas de aprendizado de máquina.'),
('Especialista em Prompts', 'Cria prompts eficazes para ferramentas de IA generativa.'),

-- Gestão e Organização
('Gerente de Projeto', 'Coordena tarefas, prazos e a comunicação da equipe.'),
('Gerente de Produto', 'Define a visão do produto e prioriza funcionalidades.'),
('Gestor de Comunidade', 'Engaja e organiza a comunidade ao redor do projeto.'),
('Scrum Master', 'Facilita processos ágeis e remove obstáculos da equipe.'),

-- DevOps e Infraestrutura
('Engenheiro DevOps', 'Cuida de deploy, integração contínua e infraestrutura.'),
('Administrador de Sistemas', 'Gerencia servidores, redes e hospedagem.'),

-- Qualidade e Testes
('Engenheiro de Qualidade (QA)', 'Planeja e executa testes para garantir a estabilidade do projeto.'),
('Testador', 'Testa funcionalidades e reporta erros ou falhas.'),

-- Artes Visuais e Multimídia
('Artista de Pixel', 'Cria arte em pixel para jogos ou visuais retrô.'),
('Artista 2D', 'Desenha personagens, cenários e elementos gráficos em 2D.'),
('Artista 3D', 'Modela e texturiza objetos e personagens em 3D.'),
('Animador', 'Cria animações para personagens e elementos visuais.'),
('Artista de Storyboard', 'Desenha cenas e sequências narrativas para planejamento visual.'),

-- Música e Áudio
('Compositor', 'Cria trilhas sonoras, músicas-tema e ambientações sonoras.'),
('Designer de Som', 'Cria efeitos sonoros e paisagens sonoras para jogos ou apps.'),
('Engenheiro de Áudio', 'Faz a mixagem e masterização de áudio digital.'),
('Dublador(a)', 'Empresta voz a personagens, interfaces ou narrações.'),

-- Narrativa e História
('Designer Narrativo', 'Constrói histórias interativas, roteiros e diálogos.'),
('Roteirista', 'Escreve roteiros para jogos, vídeos e experiências digitais.'),
('Criador de Universo', 'Desenvolve mundos fictícios, lore e ambientações.'),

-- Comunicação e Divulgação
('Gestor de Mídias Sociais', 'Gerencia redes sociais e interage com a comunidade.'),
('Estrategista de Marketing', 'Planeja ações para divulgação e engajamento.'),
('Construtor de Comunidade', 'Ajuda a reunir e engajar pessoas ao redor do projeto.'),
('Responsável por Parcerias', 'Busca colaborações, divulgação e contatos externos.'),

-- Acessibilidade e Inclusão
('Consultor de Acessibilidade', 'Garante que o projeto seja acessível a diferentes públicos.'),
('Especialista em Localização', 'Faz tradução e adaptação cultural do conteúdo.'),
('Defensor da Inclusão', 'Ajuda a promover diversidade e representatividade.'),

-- Educação e Mentoria
('Educador(a)', 'Ensina ferramentas, conceitos e práticas ao time.'),
('Padrinho de Novatos', 'Ajuda quem está entrando a se adaptar e contribuir.'),

-- Suporte e Negócios
('Consultor Jurídico', 'Orienta sobre licenças, uso de conteúdo e aspectos legais.'),
('Coordenador de Financiamento', 'Organiza campanhas de doação e busca recursos.'),
('Testador de Experiência', 'Testa produtos e dá feedback sobre usabilidade.'),
('Pesquisador de Usuário', 'Estuda como as pessoas interagem com o projeto.'),

-- Colaborações Gerais
('Mentor', 'Orienta colaboradores com menos experiência.'),
('Contribuidor(a)', 'Ajuda com melhorias, ideias ou execução de tarefas.'),
('Mantenedor(a)', 'Gerencia o repositório e coordena a evolução do projeto.'),
('Revisor(a)', 'Analisa contribuições e garante qualidade antes de publicar.');

INSERT INTO project_tools (
   name,
   description,
   hex_color
) VALUES
-- Desenvolvimento Web
('React', 'Biblioteca JavaScript para criação de interfaces de usuário.', '#61DAFB'),
('Vue.js', 'Framework progressivo para construção de interfaces.', '#42B883'),
('Angular', 'Framework completo para aplicações web.', '#DD0031'),
('Next.js', 'Framework React para SSR e aplicações fullstack.', '#000000'),
('Tailwind CSS', 'Framework CSS utilitário altamente personalizável.', '#38B2AC'),

-- Backend
('Node.js', 'Runtime JavaScript para execução no backend.', '#339933'),
('Spring Boot', 'Framework Java para criar APIs robustas e escaláveis.', '#6DB33F'),
('Django', 'Framework web em Python com ORM embutido.', '#092E20'),
('Laravel', 'Framework PHP moderno e elegante.', '#F05340'),
('Express', 'Framework leve e flexível para Node.js.', '#000000'),

-- DevOps / CI/CD
('Docker', 'Plataforma para criação e execução de containers.', '#0db7ed'),
('GitHub Actions', 'Ferramenta de CI/CD nativa do GitHub.', '#2088FF'),
('GitLab CI', 'Plataforma de integração e entrega contínua.', '#FC6D26'),
('Vercel', 'Deploy de aplicações front-end com foco em performance.', '#000000'),
('Netlify', 'Plataforma de hospedagem para sites estáticos e JAMstack.', '#00C7B7'),

-- Game Development
('Unity', 'Motor de desenvolvimento de jogos multiplataforma.', '#222C37'),
('Godot', 'Motor de jogos 2D e 3D open source.', '#478CBF'),
('Unreal Engine', 'Motor de jogos com gráficos realistas.', '#0E1128'),

-- IA / Machine Learning
('TensorFlow', 'Framework de machine learning open source.', '#FF6F00'),
('PyTorch', 'Framework de deep learning baseado em Python.', '#EE4C2C'),
('OpenCV', 'Biblioteca para visão computacional.', '#5C3EE8'),

-- Design / UI / UX
('Figma', 'Ferramenta de design colaborativo baseada em nuvem.', '#A259FF'),
('Adobe XD', 'Ferramenta de prototipagem e design UI.', '#FF61F6'),
('Canva', 'Ferramenta gráfica acessível para criação de designs.', '#00C4CC'),

-- Escrita / Docs
('Markdown', 'Linguagem de marcação leve para documentação.', '#000000'),
('Notion', 'Ferramenta de produtividade e organização.', '#000000'),
('Docsify', 'Gerador de documentação a partir de arquivos Markdown.', '#42B983'),
('MkDocs', 'Gerador de sites estáticos para documentação Python.', '#000000'),

-- Gestão / Colaboração
('Trello', 'Ferramenta de gerenciamento de tarefas baseada em quadros.', '#0079BF'),
('Jira', 'Ferramenta de gestão de projetos ágeis.', '#0052CC'),
('Slack', 'Plataforma de comunicação em equipe.', '#4A154B'),
('Discord', 'Ferramenta de voz e chat para comunidades.', '#5865F2'),
('Linear', 'Gestão de projetos moderna com foco em desenvolvedores.', '#5E6AD2');

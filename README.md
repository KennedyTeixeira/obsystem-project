# 🏢 OBSystem ERP - Sistema de Gestão Empresarial & Industrial

Sistema Integrado de Gestão Empresarial (ERP) voltado ao setor vidreiro e industrial, com gestão ponta a ponta de catálogo técnico, regras de estoque, compras, vendas, financeiro e interface moderna inspirada nos líderes de mercado.

---

## 🛠️ Stack Tecnológica

- **Backend:** Java 17, Spring Boot 3/4, Spring Data JPA, Hibernate, Bean Validation, JUnit 5.
- **Banco de Dados:** PostgreSQL (Supabase Cloud) com modelagem relacional estrita, constraints fiscais e migrações automatizadas.
- **Frontend:** React 19, TypeScript, Vite, CSS Modules puro (Design System exclusivo via Design Tokens, zero dependência de frameworks utilitários externos).
- **Testes & Qualidade:** Testes E2E cobrindo ciclos de negócio (compras, vendas e financeiro) e 100% de tipagem estrita no TypeScript.

---

## 🚀 Principais Módulos & Destaques de Engenharia

### 1. 📦 Catálogo Técnico & Regras de Estoque
- **Precificação em Tempo Real:** Cálculo automático de margens de contribuição comercial e markups.
- **Controle Preciso:** Métricas de estoque físico (atual, mínimo, máximo), estoque reservado e estoque disponível.
- **9 Cadastros de Apoio Industrial:** Tipo de Produto, Categoria, Subcategoria, Classe de Material, Modelo Construtivo, Marca/Fabricante, Unidade de Medida, Medidas Padronizadas e Milímetros (espessuras nominais).
- **Modelagem Avançada N:N:** Relacionamento Muitos-para-Muitos entre Categorias e Subcategorias com tabela associativa e junções otimizadas (`JOIN FETCH`), permitindo que subcategorias universais (ex: "INCOLOR") pertençam a múltiplas categorias (ex: "VIDROS", "BOX") sem redundância de registros.

### 2. 🔄 Esteira de Compras & Entrada de Estoque
- Fluxo transacional por etapas: `Cotação` ➔ `Pedido` ➔ `Recebimento` ➔ `Pagamento`.
- Atualização física automática do estoque e recálculo do custo médio ao receber a mercadoria.
- Geração automática de títulos de Contas a Pagar ao avançar para a etapa de pagamento.

### 3. 🛒 Esteira de Vendas & Reserva Inteligente
- Fluxo transacional: `Aberto` ➔ `Separação` ➔ `Finalizado`.
- Prevenção ativa de furo de estoque: reserva automática no momento da criação do pedido (`Disponível = Atual - Reservado`).
- Baixa física definitiva, liberação de reserva e lançamento automático de Contas a Receber na finalização.

### 4. 💰 Financeiro Integrado (Contas a Pagar & Receber)
- Títulos financeiros integrados aos módulos de compras, vendas e lançamentos operacionais avulsos.
- Rotinas completas de liquidação (baixa de títulos), cancelamento e estorno de quitação com acompanhamento de fluxo de caixa em tempo real.

---

## 🎨 Experiência do Usuário (UI/UX)

- **Grid Executivo de Produtos:** Tabela ágil com busca textual reativa e filtros combinados por categoria, subcategoria, classe e status.
- **Menu Lateral à Direita (Estilo Bling ERP):** Seções contextuais com atalho de cadastro rápido (`+ Novo Produto`) e métricas do catálogo.
- **Slide-Over Drawers:** Painéis laterais deslizantes (direita para esquerda) com CSS puro para gerenciamento dos cadastros auxiliares sem sair da tela principal.
- **Chips Interativos:** Seleção ergonômica com badges clicáveis para relacionamentos múltiplos (N:N).
- **Bottom-Sheet Drawer:** Painel deslizante inferior (baixo para cima) para ações em lote e reajuste coletivo de preços.

--
-- PostgreSQL database dump
--

\restrict SFQhyFbcDhEoHEWO12MiLjbSIeCWsLJHezqdhCPrduDxY3pXdZpQDhiaQxOcKrm

-- Dumped from database version 18.0
-- Dumped by pg_dump version 18.0

-- Started on 2025-11-14 00:37:10

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 222 (class 1259 OID 16464)
-- Name: tbalimentos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tbalimentos (
    id integer CONSTRAINT alimento_id_not_null NOT NULL,
    nome character varying(100) CONSTRAINT alimento_nome_not_null NOT NULL,
    preco numeric CONSTRAINT alimento_valor_not_null NOT NULL,
    descricao text,
    tipo_alimento character varying(10) CONSTRAINT alimento_tipo_alimento_not_null NOT NULL,
    gramas integer,
    veggie boolean,
    categoria_comida character varying(50),
    ml integer,
    zero_sugar boolean,
    tipo_bebida character varying(50),
    sabor character varying(50),
    teor_alcoolico numeric(4,2)
);


ALTER TABLE public.tbalimentos OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 16463)
-- Name: alimento_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.alimento_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.alimento_id_seq OWNER TO postgres;

--
-- TOC entry 4998 (class 0 OID 0)
-- Dependencies: 221
-- Name: alimento_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.alimento_id_seq OWNED BY public.tbalimentos.id;


--
-- TOC entry 224 (class 1259 OID 16477)
-- Name: tbpedidos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tbpedidos (
    id integer CONSTRAINT pedido_id_not_null NOT NULL,
    data_hora timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    valor_total numeric(10,2),
    usuario_id integer CONSTRAINT pedido_usuario_id_not_null NOT NULL,
    avaliacao integer
);


ALTER TABLE public.tbpedidos OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 16476)
-- Name: pedido_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pedido_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.pedido_id_seq OWNER TO postgres;

--
-- TOC entry 4999 (class 0 OID 0)
-- Dependencies: 223
-- Name: pedido_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.pedido_id_seq OWNED BY public.tbpedidos.id;


--
-- TOC entry 225 (class 1259 OID 16491)
-- Name: tbalimentos_pedido; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tbalimentos_pedido (
    pedido_id integer CONSTRAINT pedido_alimento_pedido_id_not_null NOT NULL,
    alimento_id integer CONSTRAINT pedido_alimento_alimento_id_not_null NOT NULL,
    quantidade integer DEFAULT 1 CONSTRAINT pedido_alimento_quantidade_not_null NOT NULL
);


ALTER TABLE public.tbalimentos_pedido OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 16450)
-- Name: tbusuarios; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tbusuarios (
    id integer CONSTRAINT usuario_id_not_null NOT NULL,
    nome character varying(100) CONSTRAINT usuario_nome_not_null NOT NULL,
    username character varying(50) CONSTRAINT usuario_usuario_not_null NOT NULL,
    senha character varying(50) CONSTRAINT usuario_senha_not_null NOT NULL
);


ALTER TABLE public.tbusuarios OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16449)
-- Name: usuario_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.usuario_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.usuario_id_seq OWNER TO postgres;

--
-- TOC entry 5000 (class 0 OID 0)
-- Dependencies: 219
-- Name: usuario_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.usuario_id_seq OWNED BY public.tbusuarios.id;


--
-- TOC entry 4824 (class 2604 OID 16467)
-- Name: tbalimentos id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbalimentos ALTER COLUMN id SET DEFAULT nextval('public.alimento_id_seq'::regclass);


--
-- TOC entry 4825 (class 2604 OID 16480)
-- Name: tbpedidos id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbpedidos ALTER COLUMN id SET DEFAULT nextval('public.pedido_id_seq'::regclass);


--
-- TOC entry 4823 (class 2604 OID 16453)
-- Name: tbusuarios id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbusuarios ALTER COLUMN id SET DEFAULT nextval('public.usuario_id_seq'::regclass);


--
-- TOC entry 4989 (class 0 OID 16464)
-- Dependencies: 222
-- Data for Name: tbalimentos; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tbalimentos (id, nome, preco, descricao, tipo_alimento, gramas, veggie, categoria_comida, ml, zero_sugar, tipo_bebida, sabor, teor_alcoolico) FROM stdin;
1	Pizza de Calabresa	45.50	Molho de tomate fresco, queijo mussarela, calabresa fatiada e orégano.	COMIDA	750	f	PIZZA	\N	\N	\N	\N	\N
2	Pizza Quatro Queijos	52.00	Molho de tomate, queijos mussarela, provolone, parmesão e gorgonzola.	COMIDA	780	t	PIZZA	\N	\N	\N	\N	\N
3	Pizza de Frango com Catupiry	49.90	Molho de tomate, queijo mussarela, frango desfiado e catupiry cremoso.	COMIDA	800	f	PIZZA	\N	\N	\N	\N	\N
4	Pizza Portuguesa	55.00	Molho, mussarela, presunto, ovo, cebola, pimentão e azeitonas.	COMIDA	820	f	PIZZA	\N	\N	\N	\N	\N
5	Pizza de Brócolis com Alho	48.00	Mussarela, brócolis ao alho e óleo e azeitonas pretas.	COMIDA	760	t	PIZZA	\N	\N	\N	\N	\N
6	X-Burger	28.50	Pão, hambúrguer de 150g, queijo prato e maionese da casa.	COMIDA	380	f	LANCHE	\N	\N	\N	\N	\N
7	X-Frango	28.50	Pão, hambúrguer de frango empanado de 150g, queijo cheddar e maionese da casa.	COMIDA	380	f	LANCHE	\N	\N	\N	\N	\N
8	X-Salada	32.00	Pão, hambúrguer de 150g, queijo, alface, tomate, milho e batata palha.	COMIDA	450	f	LANCHE	\N	\N	\N	\N	\N
9	X-Bacon Duplo	38.90	Pão, dois hambúrgueres de 150g, dobro de queijo e bacon crocante.	COMIDA	550	f	LANCHE	\N	\N	\N	\N	\N
10	Burger Vegetariano	35.00	Pão integral, hambúrguer de grão de bico, queijo, alface e tomate seco.	COMIDA	390	t	LANCHE	\N	\N	\N	\N	\N
11	Batata Frita Pequena	10.00	Porção de 150g de batatas fritas crocantes e sequinhas.	COMIDA	150	t	ACOMPANHAMENTO	\N	\N	\N	\N	\N
12	Batata Frita Média	12.00	Porção de 300g de batatas fritas crocantes e sequinhas.	COMIDA	300	t	ACOMPANHAMENTO	\N	\N	\N	\N	\N
13	Batata Frita Grande	15.00	Porção de 450g de batatas fritas crocantes e sequinhas.	COMIDA	450	t	ACOMPANHAMENTO	\N	\N	\N	\N	\N
14	Nuggets de Frango	18.50	Porção com 10 unidades de nuggets e molho agridoce.	COMIDA	250	f	ACOMPANHAMENTO	\N	\N	\N	\N	\N
15	Anéis de Cebola	17.00	Porção de 250g de anéis de cebola empanados e fritos.	COMIDA	250	t	ACOMPANHAMENTO	\N	\N	\N	\N	\N
16	Pudim de Leite	12.00	Fatia generosa de pudim de leite condensado com calda de caramelo.	COMIDA	150	t	SOBREMESA	\N	\N	\N	\N	\N
17	Mousse de Chocolate	14.00	Mousse de chocolate meio amargo com raspas de chocolate.	COMIDA	130	t	SOBREMESA	\N	\N	\N	\N	\N
18	Petit Gateau	22.00	Bolinho quente de chocolate com interior cremoso e bola de sorvete de creme.	COMIDA	200	t	SOBREMESA	\N	\N	\N	\N	\N
19	Torta Holandesa	16.50	Fatia de torta com base de biscoito, creme holandês e cobertura de chocolate.	COMIDA	180	t	SOBREMESA	\N	\N	\N	\N	\N
20	Açaí na Tigela 300ml	19.00	Açaí batido com banana, servido com granola e leite em pó.	COMIDA	350	t	SOBREMESA	\N	\N	\N	\N	\N
21	Coca-Cola	7.00	Lata 350ml.	BEBIDA	\N	\N	\N	350	f	REFRIGERANTE	Original	\N
22	Coca-Cola Zero	7.00	Lata 350ml.	BEBIDA	\N	\N	\N	350	t	REFRIGERANTE	Zero Açúcar	\N
23	Guaraná Antarctica	7.00	Lata 350ml.	BEBIDA	\N	\N	\N	350	f	REFRIGERANTE	Original	\N
24	Suco de Laranja Natural	9.00	Copo de 500ml feito na hora.	BEBIDA	\N	\N	\N	500	f	SUCO	Laranja	\N
25	Suco de Abacaxi com Hortelã	9.50	Copo de 500ml feito na hora.	BEBIDA	\N	\N	\N	500	f	SUCO	Abacaxi com Hortelã	\N
26	Milkshake de Morango	18.00	Copo de 400ml com sorvete de morango e calda.	BEBIDA	\N	\N	\N	400	f	MILKSHAKE	Morango	\N
27	Milkshake de Chocolate	18.00	Copo de 400ml com sorvete de chocolate e calda.	BEBIDA	\N	\N	\N	400	f	MILKSHAKE	Chocolate	\N
28	Milkshake de Baunilha	16.00	Copo de 400ml com sorvete de baunilha	BEBIDA	\N	\N	\N	400	f	MILKSHAKE	Baunilha	\N
29	Água Mineral com Gás	5.00	Garrafa 500ml.	BEBIDA	\N	\N	\N	500	f	\N	\N	\N
30	Cerveja Long Neck	12.50	Garrafa de 355ml.	BEBIDA	\N	\N	\N	355	f	\N	Puro Malte	4.80
\.


--
-- TOC entry 4992 (class 0 OID 16491)
-- Dependencies: 225
-- Data for Name: tbalimentos_pedido; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tbalimentos_pedido (pedido_id, alimento_id, quantidade) FROM stdin;
1	1	1
1	21	2
11	8	1
\.


--
-- TOC entry 4991 (class 0 OID 16477)
-- Dependencies: 224
-- Data for Name: tbpedidos; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tbpedidos (id, data_hora, valor_total, usuario_id, avaliacao) FROM stdin;
1	2025-10-17 08:31:18.885721	59.50	1	4
11	2025-11-14 00:13:02.191625	32.00	1	5
\.


--
-- TOC entry 4987 (class 0 OID 16450)
-- Dependencies: 220
-- Data for Name: tbusuarios; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tbusuarios (id, nome, username, senha) FROM stdin;
1	João Silva	joao.silva	senha123
2	Maria Oliveira	maria.oliveira	xtudo@2025
4	null 	 kakagames 	 12345 
3	Pedro Souza	pedro.souza	12345
5	null 	 1 	 1 
6	a	a	123
\.


--
-- TOC entry 5001 (class 0 OID 0)
-- Dependencies: 221
-- Name: alimento_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.alimento_id_seq', 30, true);


--
-- TOC entry 5002 (class 0 OID 0)
-- Dependencies: 223
-- Name: pedido_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.pedido_id_seq', 11, true);


--
-- TOC entry 5003 (class 0 OID 0)
-- Dependencies: 219
-- Name: usuario_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.usuario_id_seq', 6, true);


--
-- TOC entry 4831 (class 2606 OID 16475)
-- Name: tbalimentos alimento_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbalimentos
    ADD CONSTRAINT alimento_pkey PRIMARY KEY (id);


--
-- TOC entry 4835 (class 2606 OID 16499)
-- Name: tbalimentos_pedido pedido_alimento_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbalimentos_pedido
    ADD CONSTRAINT pedido_alimento_pkey PRIMARY KEY (pedido_id, alimento_id);


--
-- TOC entry 4833 (class 2606 OID 16485)
-- Name: tbpedidos pedido_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbpedidos
    ADD CONSTRAINT pedido_pkey PRIMARY KEY (id);


--
-- TOC entry 4829 (class 2606 OID 16460)
-- Name: tbusuarios usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbusuarios
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id);


--
-- TOC entry 4837 (class 2606 OID 16505)
-- Name: tbalimentos_pedido fk_alimento; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbalimentos_pedido
    ADD CONSTRAINT fk_alimento FOREIGN KEY (alimento_id) REFERENCES public.tbalimentos(id);


--
-- TOC entry 4838 (class 2606 OID 16500)
-- Name: tbalimentos_pedido fk_pedido; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbalimentos_pedido
    ADD CONSTRAINT fk_pedido FOREIGN KEY (pedido_id) REFERENCES public.tbpedidos(id);


--
-- TOC entry 4836 (class 2606 OID 16486)
-- Name: tbpedidos fk_usuario; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbpedidos
    ADD CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES public.tbusuarios(id);


-- Completed on 2025-11-14 00:37:10

--
-- PostgreSQL database dump complete
--

\unrestrict SFQhyFbcDhEoHEWO12MiLjbSIeCWsLJHezqdhCPrduDxY3pXdZpQDhiaQxOcKrm


# FundMate - Personal Finance Management

FundMate is a full-stack personal finance management application that helps users track their accounts, transactions, budgets, and scheduled payments.

## Features

- Account management with real time balance tracking
- Transaction tracking with income and expense categorization
- Budget creation and monitoring
- Scheduled transactions for recurring payments
- Monthly overview with income/expense charts
- Category management with custom icons
- Responsive design for mobile and desktop

## Technologies Used

### Stack
- React
- Spring
- PostgreSQL
- Docker
- Nginx

### Libraries and Tools
- React Router
- Axios
- JWT (JSON Web Tokens)
- Spring Scheduler
- Bootstrap
- Bootstrap Icons
- Chart.js

## Getting Started

### Prerequisites
Before getting started, make sure you have the following installed

- Docker
- Git

### Installation

1. **Clone the repository:**
```bash
git clone https://github.com/Richardson902/fundmate
cd fundmate
```

2. **Configure environment variables:**
- Create `.env.api` file
```bash
cp .env.example .env.
```
- Create `.env.data` file
```bash
cp .end.example.data .env.data
```
You can update the variables in .env.api and .env.data to secure your application or leave them as default if you're just testing.

3. **Build and run with Docker Compose:**
```bash
docker compose up --build
```

4. **Access the application:**
Once the docker containers are up and running, you can access the following URLs:
- **Development version:** `http://localhost:5173`
- **Production version:** `http://localhost:8085`
- **Database:** `http://localhost:5432`
  (Refer to the username and password in the `.env.api` file for connection details
- **API:** `http://localhost:8081`

## Usage

1. Register an account or login
2. Add your financial accounts
3. Create categories for your transactions
4. Start tracking your income and expenses
5. Set up budgets for different categories
6. Configure scheduled transactions for recurring payments
7. Monitor your financial overview on the dashboard

## Planned Improvements

- [ ] Multi-currency support
- [ ] Transaction search and filtering
- [ ] Custom date range reporting
- [ ] Category type flag (expense/income)
- [ ] Monthly overviews
- [ ] General backend improvements

## Security
- Spring Security
- JWT-based authentication
- Password encryption
- Protected API endpoints
- Input validation and sanitization


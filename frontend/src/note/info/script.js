import * as framerMotion from 'https://example.invalid/resource';
import { competitionData } from './data.js';

const { animate, inView } = framerMotion;

function renderIntroduction(data) {
    const container = document.getElementById('introduction');
    if (!container) return;
    container.innerHTML = `
        <h2 class="text-3xl md:text-4xl font-bold mb-6 text-charcoal">${data.title}</h2>
        ${data.paragraphs.map(p => `<p class="text-base md:text-lg text-gray-700 mb-4">${p}</p>`).join('')}
    `;
}

function createCard(id, content) {
    const container = document.getElementById(id);
    if (container) {
        container.innerHTML = content;
    }
}

function renderContent() {
    renderIntroduction(competitionData.introduction);

    createCard('theme-card', `
        <h3><i data-lucide="feather"></i>${competitionData.theme.title}</h3>
        <p class="mb-4">${competitionData.theme.description}</p>
        <ul>${competitionData.theme.angles.map(item => `<li><strong>${item.title}:</strong> ${item.description}</li>`).join('')}</ul>
    `);

    createCard('participants-card', `
        <h3><i data-lucide="users"></i>${competitionData.participants.title}</h3>
        <p>${competitionData.participants.description}</p>
    `);

    createCard('timeline-card', `
        <h3><i data-lucide="calendar-days"></i>${competitionData.timeline.title}</h3>
        <div class="timeline mt-6">
            ${competitionData.timeline.events.map(event => `
                <div class="timeline-item">
                    <strong>${event.phase}</strong>
                    <span>${event.date}</span>
                </div>
            `).join('')}
        </div>
    `);

    createCard('requirements-card', `
        <h3><i data-lucide="file-check-2"></i>${competitionData.requirements.title}</h3>
        <ul>${competitionData.requirements.rules.map(rule => `<li>${rule}</li>`).join('')}</ul>
    `);

    createCard('submission-card', `
        <h3><i data-lucide="send"></i>${competitionData.submission.title}</h3>
        <p>${competitionData.submission.platform.description}</p>
        <div class="text-center my-4">
             <a href="/submit-your-story" class="submit-btn">${competitionData.submission.platform.buttonText}</a>
        </div>
        <div class="email-submission">
            <p class="font-semibold">${competitionData.submission.email.title}</p>
            <p>${competitionData.submission.email.description} <code>${competitionData.submission.email.address}</code></p>
            <p class="mt-2">${competitionData.submission.email.format}</p>
            <p class="mt-2">${competitionData.submission.email.fileNameFormat}</p>
        </div>
    `);

    createCard('rules-card', `
        <h3><i data-lucide="scale"></i>${competitionData.rules.title}</h3>
        <p>${competitionData.rules.description}</p>
        <p class="font-bold my-4 text-center text-lg bg-cream p-3 rounded-lg">${competitionData.rules.formula}</p>
        <ul>${competitionData.rules.criteria.map(item => `<li><strong>${item.title}:</strong> ${item.description}</li>`).join('')}</ul>
    `);
    
    createCard('prizes-card', `
        <h3><i data-lucide="award"></i>${competitionData.prizes.title}</h3>
        <div class="prize-grid mt-6">
            ${competitionData.prizes.awards.map(award => `
                <div class="prize-item">
                    <i data-lucide="${award.icon}"></i>
                    <h4>${award.name}</h4>
                    <p>${award.reward}</p>
                </div>
            `).join('')}
        </div>
    `);

    createCard('consultation-card', `
        <h3><i data-lucide="messages-square"></i>${competitionData.consultation.title}</h3>
        <p>${competitionData.consultation.description}</p>
    `);

    createCard('notes-card', `
        <h3><i data-lucide="alert-triangle"></i>${competitionData.notes.title}</h3>
        <ul>${competitionData.notes.points.map(point => `<li><strong>${point.title}:</strong> ${point.description}</li>`).join('')}</ul>
    `);

    createCard('organizer-card', `
        <h3><i data-lucide="shield"></i>${competitionData.organizer.title}</h3>
        <p class="text-2xl font-bold text-center py-8 text-accent-green">${competitionData.organizer.name}</p>
    `);
}

function animateOnScroll() {
    const elements = document.querySelectorAll('.content-card, .content-section, header');
    elements.forEach(el => {
        inView(el, () => {
            animate(el, { opacity: [0, 1], y: [30, 0] }, { duration: 0.7, delay: 0.1 });
        }, { amount: 0.2 });
    });
}


document.addEventListener('DOMContentLoaded', () => {
    renderContent();
    lucide.createIcons();
    animateOnScroll();
});
